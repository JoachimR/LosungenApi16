package de.reiss.android.losungen.main.content

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import de.reiss.android.losungen.App
import de.reiss.android.losungen.DaysPositionUtil
import de.reiss.android.losungen.R
import de.reiss.android.losungen.architecture.AppFragment
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.events.DatabaseRefreshed
import de.reiss.android.losungen.events.FontSizeChanged
import de.reiss.android.losungen.formattedDate
import de.reiss.android.losungen.model.DailyLosung
import de.reiss.android.losungen.model.Note
import de.reiss.android.losungen.note.edit.EditNoteActivity
import de.reiss.android.losungen.preferences.AppPreferences
import de.reiss.android.losungen.preferences.AppPreferencesActivity
import de.reiss.android.losungen.util.copyToClipboard
import de.reiss.android.losungen.util.extensions.*
import de.reiss.android.losungen.util.htmlize
import kotlinx.android.synthetic.main.losung_content.*
import kotlinx.android.synthetic.main.losung_empty.*
import kotlinx.android.synthetic.main.losung_fragment.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class LosungFragment : AppFragment<LosungViewModel>(R.layout.losung_fragment) {

    companion object {

        private const val KEY_POSITION = "KEY_POSITION"

        fun createInstance(position: Int) = LosungFragment().apply {
            arguments = Bundle().apply {
                putInt(KEY_POSITION, position)
            }
        }

    }

    private val appPreferences: AppPreferences by lazy {
        App.component.appPreferences
    }

    private var position = -1

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

    override fun initViews() {
        losung_note_edit.onClick {
            viewModel.losung()?.let { losung ->
                activity?.let {
                    it.startActivity(EditNoteActivity.createIntent(
                            context = it,
                            date = losung.date,
                            losungContent = losung.content
                    ))
                }
            }
        }

        losung_change_translation.onClick {
            activity?.let {
                it.startActivity(AppPreferencesActivity.createIntent(it))
            }
        }

        losung_source1.setOnLongClickListener {
            context?.let {
                copyToClipboard(it, losung_source1.text.toString())
                showShortSnackbar(message = R.string.copied_to_clipboard)
            }
            true
        }

        losung_source2.setOnLongClickListener {
            context?.let {
                copyToClipboard(it, losung_source2.text.toString())
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
    fun onMessageEvent(event: FontSizeChanged) {
        updateStyle()
    }

    override fun initViewModelObservers() {
        viewModel.losungLiveData().observe(this, Observer<AsyncLoad<DailyLosung>> {
            updateUi()
        })
        viewModel.noteLiveData().observe(this, Observer<AsyncLoad<Note>> {
            updateUi()
        })
    }

    private fun updateUi() {
        val context = context ?: return
        val losung = viewModel.losung()

        losung_date.text = formattedDate(context, date().time)

        when {

            viewModel.isLoadingLosung() -> {
                losung_loading.loading = true
            }

            (viewModel.isErrorForLosung() || losung == null) -> {
                losung_loading.loading = false
                losung_empty_root.visibility = VISIBLE
                losung_content_root.visibility = GONE
            }

            viewModel.isSuccessForLosung() -> {
                losung_loading.loading = false
                losung_empty_root.visibility = GONE
                losung_content_root.visibility = VISIBLE

                losung_text1.text = htmlize(losung.content.text1)
                losung_source1.text = losung.content.source1
                losung_text2.text = htmlize(losung.content.text2)
                losung_source2.text = losung.content.source2
            }
        }

        activity?.invalidateOptionsMenu()

        updateStyle()
    }

    private fun updateStyle() {
        val size = appPreferences.fontSize()
        val contentSize = (size * 1.1).toFloat()
        val sourceSize = (size * 0.7).toFloat()
        val noteHeaderSize = (size * 0.6).toFloat()
        val noteContentSize = (size * 0.75).toFloat()

        losung_date.textSize = contentSize
        losung_text1.textSize = contentSize
        losung_source1.textSize = sourceSize
        losung_text2.textSize = contentSize
        losung_source2.textSize = sourceSize
        losung_note_header.textSize = noteHeaderSize
        losung_note_content.textSize = sourceSize
        losung_note_edit.textSize = noteContentSize

        losung_note.visibleElseGone(appPreferences.showNotes())
        if (viewModel.isLoadingNote().not()) {
            losung_note_content.text = viewModel.note()?.noteText ?: ""
        }

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
                        time = losung.date.time,
                        losungContent = losung.content,
                        note = viewModel.note()?.noteText ?: ""
                ))
            }
        }
    }

}