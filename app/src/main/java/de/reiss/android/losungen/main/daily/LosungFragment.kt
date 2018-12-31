package de.reiss.android.losungen.main.daily

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import de.reiss.android.losungen.App
import de.reiss.android.losungen.DaysPositionUtil
import de.reiss.android.losungen.R
import de.reiss.android.losungen.architecture.AppFragment
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.events.AppStyleChanged
import de.reiss.android.losungen.events.DatabaseRefreshed
import de.reiss.android.losungen.formattedDate
import de.reiss.android.losungen.model.DailyLosung
import de.reiss.android.losungen.model.Note
import de.reiss.android.losungen.note.edit.EditNoteActivity
import de.reiss.android.losungen.preferences.AppPreferences
import de.reiss.android.losungen.preferences.AppPreferencesActivity
import de.reiss.android.losungen.util.copyToClipboard
import de.reiss.android.losungen.util.extensions.*
import de.reiss.android.losungen.util.htmlize
import de.reiss.android.losungen.util.view.FadingProgressBar
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import android.content.Intent
import android.net.Uri


@Suppress("PropertyName", "PrivatePropertyName")
abstract class LosungFragment(@LayoutRes private val fragmentLayout: Int)
    : AppFragment<LosungViewModel>(fragmentLayout) {

    companion object {

        private const val KEY_POSITION = "KEY_POSITION"

    }

    private lateinit var root: View
    protected lateinit var text1root: View
    private lateinit var text1: TextView
    private lateinit var source1: TextView
    protected lateinit var text2root: View
    protected lateinit var text2: TextView
    private lateinit var source2: TextView
    protected lateinit var date: TextView
    protected lateinit var loading: FadingProgressBar
    private lateinit var empty_root: View
    private lateinit var check_for_update: TextView
    private lateinit var content_root: View
    protected lateinit var note_root: View
    private lateinit var note_header: TextView
    private lateinit var note_content: TextView
    protected lateinit var note_edit: View

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
            ViewModelProviders.of(this, LosungViewModel.Factory(
                    App.component.losungRepository))

    override fun defineViewModel(): LosungViewModel =
            loadViewModelProvider().get(LosungViewModel::class.java)

    override fun initViews(layout: View) {

        findViews(layout)

        note_edit.onClick {
            viewModel.losung()?.let { losung ->
                activity?.let {
                    it.startActivity(EditNoteActivity.createIntent(
                            context = it,
                            date = losung.startDate(),
                            bibleTextPair = losung.bibleTextPair
                    ))
                }
            }
        }

        check_for_update.onClick {
            activity?.let {
                val appPackageName = it.getPackageName()
                try {
                    it.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
                } catch (anfe: android.content.ActivityNotFoundException) {
                    it.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
                }
            }
        }

        source1.setOnLongClickListener {
            context?.let {
                copyToClipboard(it, source1.text.toString())
                showShortSnackbar(message = R.string.copied_to_clipboard)
            }
            true
        }

        source2.setOnLongClickListener {
            context?.let {
                copyToClipboard(it, source2.text.toString())
                showShortSnackbar(message = R.string.copied_to_clipboard)
            }
            true
        }
    }

    /**
     *  Need findViewById() here
     *  because ids are reused in different xml layouts
     */
    open fun findViews(layout: View) {
        root = layout.findViewById(R.id.losung_root)
        text1root = layout.findViewById(R.id.losung_text1_root)
        text1 = layout.findViewById(R.id.losung_text1)
        source1 = layout.findViewById(R.id.losung_source1)
        text2root = layout.findViewById(R.id.losung_text2_root)
        text2 = layout.findViewById(R.id.losung_text2)
        source2 = layout.findViewById(R.id.losung_source2)
        date = layout.findViewById(R.id.losung_date)
        loading = layout.findViewById(R.id.losung_loading)
        empty_root = layout.findViewById(R.id.losung_empty_root)
        check_for_update = layout.findViewById(R.id.losung_check_for_update)
        content_root = layout.findViewById(R.id.losung_content_root)
        note_root = layout.findViewById(R.id.losung_note_root)
        note_header = layout.findViewById(R.id.losung_note_header)
        note_content = layout.findViewById(R.id.losung_note_content)
        note_edit = layout.findViewById(R.id.losung_note_edit)
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

        date.text = formattedDate(context, date().time)

        when {

            viewModel.isLoadingLosung() -> {
                loading.loading = true
            }

            (viewModel.isErrorForLosung() || losung == null) -> {
                loading.loading = false
                empty_root.visibility = VISIBLE
                content_root.visibility = GONE
            }

            viewModel.isSuccessForLosung() -> {
                loading.loading = false
                empty_root.visibility = GONE
                content_root.visibility = VISIBLE

                text1.text = htmlize(losung.bibleTextPair.first.text)
                source1.text = losung.bibleTextPair.first.source
                text2.text = htmlize(losung.bibleTextPair.second.text)
                source2.text = losung.bibleTextPair.second.source
            }
        }

        activity?.invalidateOptionsMenu()

        updateStyle(losung)
    }

    open fun updateStyle(losung: DailyLosung?) {
        date.setPadding(date.paddingLeft, dateTopPadding(), date.paddingRight, date.paddingBottom)

        val size = appPreferences.fontSize()
        val contentSize = (size * 1.1).toFloat()
        val sourceSize = (size * 0.7).toFloat()
        val noteHeaderSize = (size * 0.6).toFloat()
        val noteContentSize = (size * 0.75).toFloat()

        date.textSize = contentSize
        text1.textSize = contentSize
        source1.textSize = sourceSize
        text2.textSize = contentSize
        source2.textSize = sourceSize
        note_header.textSize = noteHeaderSize
        note_content.textSize = noteContentSize

        note_root.visibleElseGone(losung != null && appPreferences.showNotes())
        if (viewModel.isLoadingNote().not()) {
            note_content.text = viewModel.note()?.noteText ?: ""
        }

        setFontColor(appPreferences.fontColor())

        setTypeFace(appPreferences.typeface())

        root.setBackgroundColor(appPreferences.backgroundColor())
    }

    open fun setFontColor(fontColor: Int) {
        check_for_update.setTextColor(fontColor)
        date.setTextColor(fontColor)
        text1.setTextColor(fontColor)
        source1.setTextColor(fontColor)
        text2.setTextColor(fontColor)
        source2.setTextColor(fontColor)
        note_header.setTextColor(fontColor)
        note_content.setTextColor(fontColor)
    }

    open fun setTypeFace(typeface: Typeface) {
        check_for_update.typeface = typeface
        date.typeface = typeface
        text1.typeface = typeface
        source1.typeface = typeface
        text2.typeface = typeface
        source2.typeface = typeface
        note_header.typeface = typeface
        note_content.typeface = typeface
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
                displayDialog(ShareDialog.createInstance(
                        context = context,
                        time = losung.startDate().time,
                        bibleTextPair = losung.bibleTextPair,
                        note = viewModel.note()?.noteText ?: ""
                ))
            }
        }
    }

    private fun dateTopPadding() =
            if (appPreferences.showToolbar())
                dateTopPaddingWithToolbar(context!!)
            else
                dateTopPaddingNoToolbar(context!!)

    private fun dateTopPaddingWithToolbar(context: Context) = context.dipToPx(16f)

    private fun dateTopPaddingNoToolbar(context: Context) = context.dipToPx(36f)

}