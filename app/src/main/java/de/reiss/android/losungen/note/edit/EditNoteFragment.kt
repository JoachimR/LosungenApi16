package de.reiss.android.losungen.note.edit

import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import de.reiss.android.losungen.App
import de.reiss.android.losungen.R
import de.reiss.android.losungen.architecture.AppFragment
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.databinding.EditNoteFragmentBinding
import de.reiss.android.losungen.main.daily.ShareDialog
import de.reiss.android.losungen.model.BibleTextPair
import de.reiss.android.losungen.model.Note
import de.reiss.android.losungen.preferences.AppPreferences
import de.reiss.android.losungen.util.extensions.hideKeyboard
import de.reiss.android.losungen.util.extensions.onClick
import de.reiss.android.losungen.util.extensions.showLongSnackbar
import de.reiss.android.losungen.util.extensions.withZeroDayTime
import java.util.*

class EditNoteFragment :
    AppFragment<EditNoteFragmentBinding, EditNoteViewModel>(R.layout.edit_note_fragment) {

    companion object {

        private const val KEY_TIME = "KEY_TIME"
        private const val KEY_BIBLE_TEXT_PAIR = "KEY_BIBLE_TEXT_PAIR"
        private const val KEY_PRE_FILL_TEXT_DONE = "KEY_PRE_FILL_TEXT_DONE"

        fun createInstance(
            time: Long,
            bibleTextPair: BibleTextPair
        ) = EditNoteFragment().apply {
            arguments = Bundle().apply {
                putLong(KEY_TIME, time)
                putParcelable(KEY_BIBLE_TEXT_PAIR, bibleTextPair)
            }
        }

    }

    private var time = -1L
    private lateinit var bibleTextPair: BibleTextPair

    private var preFillTextDone = false

    private val appPreferences: AppPreferences by lazy {
        App.component.appPreferences
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        time = arguments?.getLong(KEY_TIME, -1) ?: -1
        if (time < 0) {
            throw IllegalStateException("no time given")
        }
        bibleTextPair = arguments?.getParcelable(KEY_BIBLE_TEXT_PAIR)
            ?: throw IllegalStateException("no losung content given")

        preFillTextDone = savedInstanceState?.getBoolean(KEY_PRE_FILL_TEXT_DONE) ?: false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(KEY_PRE_FILL_TEXT_DONE, preFillTextDone)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit_note, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        viewModel.isLoadingOrStoring().let { loading ->
            menu.findItem(R.id.menu_edit_note_save).isVisible = loading.not()
            menu.findItem(R.id.menu_edit_note_save_disabled).isVisible = loading
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_edit_note_save -> {
                activity?.hideKeyboard()
                tryStore()
                return true
            }

            R.id.menu_edit_note_share -> {
                activity?.let { activity ->
                    viewModel.note()?.let { note ->
                        activity.hideKeyboard()
                        displayDialog(
                            ShareDialog.createInstance(
                                context = activity,
                                time = note.date.time,
                                bibleTextPair = bibleTextPair,
                                note = note.noteText
                            )
                        )
                        return true
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun defineViewModelProvider(): ViewModelProvider =
        ViewModelProviders.of(
            this, EditNoteViewModel.Factory(
                App.component.editNoteRepository
            )
        )

    override fun defineViewModel(): EditNoteViewModel =
        loadViewModelProvider().get(EditNoteViewModel::class.java)


    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        EditNoteFragmentBinding.inflate(inflater, container, false)

    override fun initViews() {

        binding.editNoteLoadErrorRetry.onClick {
            tryLoad()
        }

        binding.editNoteRoot.setBackgroundColor(appPreferences.backgroundColor())
        val fontColor = appPreferences.fontColor()
        binding.editNoteInput.setTextColor(fontColor)
        binding.editNoteInput.setHintTextColor(fontColor)
        binding.editNoteInput.textSize = (appPreferences.fontSize() * 1.1).toFloat()
    }

    override fun initViewModelObservers() {
        viewModel.loadNoteLiveData().observe(this, Observer<AsyncLoad<Note?>> {
            updateUi()
        })
        viewModel.storeNoteLiveData().observe(this, Observer<AsyncLoad<Void>> {
            updateUi()
        })
    }

    override fun onAppFragmentReady() {
        if (viewModel.note() == null) {
            tryLoad()
        }
    }

    private fun updateUi() {
        when {
            viewModel.storeSuccess() -> {
                activity?.supportFinishAfterTransition()
            }

            viewModel.isLoadingOrStoring() -> {
                binding.editNoteLoading.loading = true
                binding.editNoteInput.visibility = GONE
                binding.editNoteLoadError.visibility = GONE
            }

            viewModel.loadError() -> {
                binding.editNoteLoading.loading = false
                binding.editNoteInput.visibility = GONE
                binding.editNoteLoadError.visibility = VISIBLE
            }

            viewModel.storeError() -> {
                binding.editNoteLoading.loading = false
                binding.editNoteInput.visibility = VISIBLE
                binding.editNoteLoadError.visibility = GONE
                showLongSnackbar(
                    message = R.string.edit_note_store_error,
                    action = {
                        tryStore()
                    },
                    callback = {
                        viewModel.onStoreErrorShown()
                    })
            }

            else -> {
                binding.editNoteLoading.loading = false
                binding.editNoteInput.visibility = VISIBLE
                binding.editNoteLoadError.visibility = GONE

                if (preFillTextDone.not()) {
                    viewModel.note()?.let {
                        binding.editNoteInput.setText(it.noteText)
                        preFillTextDone = true
                    }
                }
            }
        }

        activity?.invalidateOptionsMenu()
    }

    private fun tryLoad() {
        if (viewModel.isLoadingOrStoring().not()) {
            viewModel.loadNote(Date(time).withZeroDayTime())
        }
    }

    private fun tryStore() {
        if (viewModel.isLoadingOrStoring().not()) {
            viewModel.storeNote(
                date = Date(time).withZeroDayTime(),
                text = binding.editNoteInput.text.toString(),
                bibleTextPair = bibleTextPair
            )
        }
    }

}
