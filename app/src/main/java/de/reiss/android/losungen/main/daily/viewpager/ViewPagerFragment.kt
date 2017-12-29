package de.reiss.android.losungen.main.daily.viewpager


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import de.reiss.android.losungen.App
import de.reiss.android.losungen.DaysPositionUtil
import de.reiss.android.losungen.R
import de.reiss.android.losungen.architecture.AppFragment
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.events.DatabaseRefreshed
import de.reiss.android.losungen.events.ViewPagerMoveRequest
import de.reiss.android.losungen.events.postMessageEvent
import de.reiss.android.losungen.util.extensions.registerToEventBus
import de.reiss.android.losungen.util.extensions.unregisterFromEventBus
import kotlinx.android.synthetic.main.view_pager_fragment.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class ViewPagerFragment : AppFragment<ViewPagerViewModel>(R.layout.view_pager_fragment) {

    companion object {

        private val KEY_INITIAL_POS = "KEY_INITIAL_POS"
        private val KEY_CURRENT_POSITION = "KEY_CURRENT_POSITION"

        private val INVALID_POSITION = -1

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

    override fun initViews(layout: View) {
        adapter = adapterCreator.create(childFragmentManager)
        view_pager.adapter = adapter
    }

    override fun defineViewModelProvider(): ViewModelProvider =
            appPreferences.chosenLanguage.let { chosenLanguage ->
                if (chosenLanguage == null) {
                    throw IllegalStateException("No language chosen")
                }
                return ViewModelProviders.of(this,
                        ViewPagerViewModel.Factory(chosenLanguage,
                                App.component.viewPagerRepository))
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
        view_pager.currentItem = positionInFocus
    }

    private fun tryRefresh() {
        if (viewModel.isLoadingContent().not()) {
            appPreferences.chosenLanguage?.let { chosenLanguage ->
                viewModel.prepareContentFor(
                        language = chosenLanguage,
                        date = DaysPositionUtil.dayFor(savedPosition).time)
            }
        }
    }

    private fun updateUi() {
        if (viewModel.isLoadingContent()) {
            viewModel.currentLanguage().let {
                if (it == null) {
                    throw IllegalStateException("Loading unknown content")
                } else {
                    view_pager_loading_text.text =
                            getString(R.string.view_pager_loading_content, it)
                }
            }
            view_pager_loading.visibility = View.VISIBLE
            view_pager.visibility = View.GONE
        } else {
            view_pager_loading.visibility = View.GONE
            view_pager.visibility = View.VISIBLE
            postMessageEvent(DatabaseRefreshed())
        }
    }

    private fun currentPosition() = view_pager.currentItem

}
