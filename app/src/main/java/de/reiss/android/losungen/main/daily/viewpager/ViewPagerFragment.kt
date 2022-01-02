package de.reiss.android.losungen.main.daily.viewpager


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import de.reiss.android.losungen.App
import de.reiss.android.losungen.DaysPositionUtil
import de.reiss.android.losungen.R
import de.reiss.android.losungen.architecture.AppFragment
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.databinding.ViewPagerFragmentBinding
import de.reiss.android.losungen.events.DatabaseRefreshed
import de.reiss.android.losungen.events.ViewPagerMoveRequest
import de.reiss.android.losungen.events.postMessageEvent
import de.reiss.android.losungen.util.extensions.registerToEventBus
import de.reiss.android.losungen.util.extensions.unregisterFromEventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class ViewPagerFragment :
    AppFragment<ViewPagerFragmentBinding, ViewPagerViewModel>(R.layout.view_pager_fragment) {

    companion object {

        private const val KEY_INITIAL_POS = "KEY_INITIAL_POS"
        private const val KEY_CURRENT_POSITION = "KEY_CURRENT_POSITION"

        private const val INVALID_POSITION = -1

        fun createInstance(position: Int? = null) = ViewPagerFragment().apply {
            arguments = Bundle().apply {
                if (position != null) {
                    putInt(KEY_INITIAL_POS, position)
                }
            }
        }
    }

    private val appPreferences by lazy {
        App.component.appPreferences
    }

    private var savedPosition = INVALID_POSITION

    var adapterCreator: ViewPagerAdapterCreator = ViewPagerAdapterCreator()

    private lateinit var adapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadPosition(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        registerToEventBus()

        if (appPreferences.chosenLanguage != viewModel.currentLanguage()) {
            tryRefresh()
        }
    }

    override fun onStop() {
        unregisterFromEventBus()
        super.onStop()
    }

    private fun loadPosition(savedInstanceState: Bundle?) {
        val initialPos = arguments?.getInt(KEY_INITIAL_POS, INVALID_POSITION) ?: -1
        arguments?.remove(KEY_INITIAL_POS)
        savedPosition = when {
            initialPos != INVALID_POSITION -> initialPos
            else -> savedInstanceState?.getInt(KEY_CURRENT_POSITION)
                ?: DaysPositionUtil.positionFor(Calendar.getInstance())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(KEY_CURRENT_POSITION, currentPosition())
        super.onSaveInstanceState(outState)
    }

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        ViewPagerFragmentBinding.inflate(inflater, container, false)

    override fun initViews() {
        adapter = adapterCreator.create(childFragmentManager)
        binding.viewPager.adapter = adapter
    }

    override fun defineViewModelProvider(): ViewModelProvider =
        appPreferences.chosenLanguage.let { chosenLanguage ->
            if (chosenLanguage == null) {
                throw IllegalStateException("No language chosen")
            }
            return ViewModelProviders.of(
                this,
                ViewPagerViewModel.Factory(
                    chosenLanguage,
                    App.component.viewPagerRepository
                )
            )
        }

    override fun defineViewModel(): ViewPagerViewModel =
        loadViewModelProvider().get(ViewPagerViewModel::class.java)

    override fun initViewModelObservers() {
        viewModel.loadYearLiveData().observe(this, Observer<AsyncLoad<String>> {
            updateUi()
        })
    }

    override fun onAppFragmentReady() {
        goToPosition(savedPosition)
        updateUi()
        tryRefresh()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: ViewPagerMoveRequest) {
        goToPosition(event.position)
    }

    private fun goToPosition(positionInFocus: Int) {
        binding.viewPager.currentItem = positionInFocus
    }

    private fun tryRefresh() {
        if (viewModel.isLoadingContent().not()) {
            appPreferences.chosenLanguage?.let { chosenLanguage ->
                viewModel.prepareContentFor(
                    language = chosenLanguage,
                    date = DaysPositionUtil.dayFor(savedPosition).time
                )
            }
        }
    }

    private fun updateUi() {
        if (viewModel.isLoadingContent()) {
            viewModel.currentLanguage().let {
                if (it == null) {
                    throw IllegalStateException("Loading unknown content")
                } else {
                    binding.viewPagerLoadingText.text =
                        getString(R.string.view_pager_loading_content, it)
                }
            }
            binding.viewPagerLoading.visibility = View.VISIBLE
            binding.viewPager.visibility = View.GONE
        } else {
            binding.viewPagerLoading.visibility = View.GONE
            binding.viewPager.visibility = View.VISIBLE
            postMessageEvent(DatabaseRefreshed())
        }
    }

    private fun currentPosition() = binding.viewPager.currentItem

}
