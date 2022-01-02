package de.reiss.android.losungen.main.daily

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import de.reiss.android.losungen.App
import de.reiss.android.losungen.DaysPositionUtil
import de.reiss.android.losungen.R
import de.reiss.android.losungen.architecture.AppFragment
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.databinding.LosungFragmentWithCardsBinding
import de.reiss.android.losungen.events.AppStyleChanged
import de.reiss.android.losungen.events.DatabaseRefreshed
import de.reiss.android.losungen.formattedDate
import de.reiss.android.losungen.model.DailyLosung
import de.reiss.android.losungen.model.Note
import de.reiss.android.losungen.note.edit.EditNoteActivity
import de.reiss.android.losungen.preferences.AppPreferences
import de.reiss.android.losungen.util.copyToClipboard
import de.reiss.android.losungen.util.extensions.*
import de.reiss.android.losungen.util.htmlize
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class LosungFragmentWithCards :
    AppFragment<LosungFragmentWithCardsBinding, LosungViewModel>(R.layout.losung_fragment_with_cards) {

    companion object {

        private const val KEY_POSITION = "KEY_POSITION"

        fun createInstance(position: Int) = LosungFragmentWithCards().apply {
            arguments = Bundle().apply {
                putInt(KEY_POSITION, position)
            }
        }

    }

    protected val appPreferences: AppPreferences by lazy {
        App.component.appPreferences
    }

    protected var position = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        position = arguments?.getInt(KEY_POSITION, -1) ?: -1
        if (position < 0) {
            throw IllegalStateException("date position unknown")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.menu_share).isEnabled = viewModel.losung() != null
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.menu_share -> {
                share()
                true
            }
            R.id.menu_font_size -> {
                displayDialog(FontSizePreferenceDialog.createInstance())
                true
            }
            R.id.menu_date_pick -> {
                displayDialog(ChooseDayDialog.createInstance(position))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun onStart() {
        super.onStart()
        registerToEventBus()
        tryLoad()
    }

    override fun onStop() {
        unregisterFromEventBus()
        super.onStop()
    }

    override fun defineViewModelProvider(): ViewModelProvider =
        ViewModelProviders.of(
            this, LosungViewModel.Factory(
                App.component.losungRepository
            )
        )

    override fun defineViewModel(): LosungViewModel =
        loadViewModelProvider().get(LosungViewModel::class.java)


    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        LosungFragmentWithCardsBinding.inflate(inflater, container, false)

    override fun initViews() {


        binding.content.losungNoteEdit.onClick {
            viewModel.losung()?.let { losung ->
                activity?.let {
                    it.startActivity(
                        EditNoteActivity.createIntent(
                            context = it,
                            date = losung.startDate(),
                            bibleTextPair = losung.bibleTextPair
                        )
                    )
                }
            }
        }

        binding.content.losungEmptyRoot.losungCheckForUpdate.onClick {
            activity?.let {
                val appPackageName = it.getPackageName()
                try {
                    it.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=$appPackageName")
                        )
                    )
                } catch (anfe: android.content.ActivityNotFoundException) {
                    it.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                        )
                    )
                }
            }
        }

        binding.content.losungSource1.setOnLongClickListener {
            context?.let {
                copyToClipboard(it, binding.content.losungSource1.text.toString())
                showShortSnackbar(message = R.string.copied_to_clipboard)
            }
            true
        }

        binding.content.losungSource2.setOnLongClickListener {
            context?.let {
                copyToClipboard(it, binding.content.losungSource2.text.toString())
                showShortSnackbar(message = R.string.copied_to_clipboard)
            }
            true
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: DatabaseRefreshed) {
        tryLoad()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: AppStyleChanged) {
        updateStyle(viewModel.losung())
    }

    override fun initViewModelObservers() {
        viewModel.losungLiveData().observe(this, Observer<AsyncLoad<DailyLosung?>> {
            updateUi()
        })
        viewModel.noteLiveData().observe(this, Observer<AsyncLoad<Note?>> {
            updateUi()
        })
    }

    private fun updateUi() {
        val context = context ?: return
        val losung = viewModel.losung()

        binding.content.losungDate.text = formattedDate(context, date().time)

        when {

            viewModel.isLoadingLosung() -> {
                binding.losungLoading.loading = true
            }

            (viewModel.isErrorForLosung() || losung == null) -> {
                binding.losungLoading.loading = false
                binding.content.losungEmptyRoot.root.visibility = View.VISIBLE
                binding.content.losungContentRoot.visibility = View.GONE
            }

            viewModel.isSuccessForLosung() -> {
                binding.losungLoading.loading = false
                binding.content.losungEmptyRoot.root.visibility = View.GONE
                binding.content.losungContentRoot.visibility = View.VISIBLE

                binding.content.losungText1.text = htmlize(losung.bibleTextPair.first.text)
                binding.content.losungSource1.text = losung.bibleTextPair.first.source
                binding.content.losungText2.text = htmlize(losung.bibleTextPair.second.text)
                binding.content.losungSource2.text = losung.bibleTextPair.second.source
            }
        }

        activity?.invalidateOptionsMenu()

        updateStyle(losung)
    }

    fun updateStyle(losung: DailyLosung?) {
        binding.content.losungText1Root.setCardBackgroundColor(
            appPreferences.cardBackgroundColor()
        )
        binding.content.losungText2Root.setCardBackgroundColor(
            appPreferences.cardBackgroundColor()
        )
        binding.content.losungNoteRoot.setCardBackgroundColor(
            appPreferences.cardBackgroundColor()
        )

        binding.content.losungDate.setPadding(
            binding.content.losungDate.paddingLeft,
            dateTopPadding(),
            binding.content.losungDate.paddingRight,
            binding.content.losungDate.paddingBottom
        )

        val size = appPreferences.fontSize()
        val contentSize = (size * 1.1).toFloat()
        val sourceSize = (size * 0.7).toFloat()
        val noteHeaderSize = (size * 0.6).toFloat()
        val noteContentSize = (size * 0.75).toFloat()

        binding.content.losungDate.textSize = contentSize
        binding.content.losungText1.textSize = contentSize
        binding.content.losungSource1.textSize = sourceSize
        binding.content.losungText2.textSize = contentSize
        binding.content.losungSource2.textSize = sourceSize
        binding.content.losungNoteHeader.textSize = noteHeaderSize
        binding.content.losungNoteContent.textSize = noteContentSize

        binding.content.losungNoteRoot.visibleElseGone(losung != null && appPreferences.showNotes())
        if (viewModel.isLoadingNote().not()) {
            binding.content.losungNoteContent.text = viewModel.note()?.noteText ?: ""
        }

        setFontColor(appPreferences.fontColor())

        setTypeFace(appPreferences.typeface())

        binding.root.setBackgroundColor(appPreferences.backgroundColor())
    }

    fun setFontColor(fontColor: Int) {
        binding.content.losungEmptyRoot.losungCheckForUpdate.setTextColor(fontColor)
        binding.content.losungDate.setTextColor(fontColor)
        binding.content.losungText1.setTextColor(fontColor)
        binding.content.losungSource1.setTextColor(fontColor)
        binding.content.losungText2.setTextColor(fontColor)
        binding.content.losungSource2.setTextColor(fontColor)
        binding.content.losungNoteHeader.setTextColor(fontColor)
        binding.content.losungNoteContent.setTextColor(fontColor)
        binding.content.losungNoteEdit.setTextColor(fontColor)
    }

    fun setTypeFace(typeface: Typeface) {
        binding.content.losungEmptyRoot.losungCheckForUpdate.typeface = typeface
        binding.content.losungDate.typeface = typeface
        binding.content.losungText1.typeface = typeface
        binding.content.losungSource1.typeface = typeface
        binding.content.losungText2.typeface = typeface
        binding.content.losungSource2.typeface = typeface
        binding.content.losungNoteHeader.typeface = typeface
        binding.content.losungNoteContent.typeface = typeface
    }

    private fun tryLoad() {
        val date = date()

        if (viewModel.isLoadingLosung().not()) {
            viewModel.loadLosung(date)
        }
        if (viewModel.isLoadingNote().not()) {
            viewModel.loadNote(date)
        }
    }

    private fun date() = DaysPositionUtil.dayFor(position).time

    private fun share() {
        context?.let { context ->
            viewModel.losung()?.let { losung ->
                displayDialog(
                    ShareDialog.createInstance(
                        context = context,
                        time = losung.startDate().time,
                        bibleTextPair = losung.bibleTextPair,
                        note = viewModel.note()?.noteText ?: ""
                    )
                )
            }
        }
    }

    private fun dateTopPadding() =
        if (appPreferences.showToolbar())
            dateTopPaddingWithToolbar(requireContext())
        else
            dateTopPaddingNoToolbar(requireContext())

    private fun dateTopPaddingWithToolbar(context: Context) = context.dipToPx(16f)

    private fun dateTopPaddingNoToolbar(context: Context) = context.dipToPx(36f)

}