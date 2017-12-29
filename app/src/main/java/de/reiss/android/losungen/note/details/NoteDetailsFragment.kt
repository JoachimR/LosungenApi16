package de.reiss.android.losungen.note.details


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import de.reiss.android.losungen.App
import de.reiss.android.losungen.R
import de.reiss.android.losungen.architecture.AppFragment
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.main.daily.ShareDialog
import de.reiss.android.losungen.model.Note
import de.reiss.android.losungen.note.edit.EditNoteActivity
import de.reiss.android.losungen.util.contentAsString
import de.reiss.android.losungen.util.extensions.showIndefiniteSnackbar
import kotlinx.android.synthetic.main.note_details_fragment.*


class NoteDetailsFragment : AppFragment<NoteDetailsViewModel>(R.layout.note_details_fragment) {

    companion object {

        private const val KEY_NOTE = "KEY_NOTE"

        fun createInstance(note: Note) = NoteDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_NOTE, note)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_note_details, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val enable = viewModel.isDeleting().not()
        menu.findItem(R.id.menu_note_details_share).isEnabled = enable
        menu.findItem(R.id.menu_note_details_delete).isEnabled = enable
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                R.id.menu_note_details_share -> {
                    onShareClicked()
                    true
                }
                R.id.menu_note_details_edit -> {
                    onEditClicked()
                    true
                }
                R.id.menu_note_details_delete -> {
                    onDeleteClicked()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    override fun initViews(layout: View) {
    }

    override fun defineViewModelProvider(): ViewModelProvider =
            ViewModelProviders.of(this, NoteDetailsViewModel.Factory(
                    arguments!!.getParcelable(KEY_NOTE), App.component.noteDetailsRepository))

    override fun defineViewModel(): NoteDetailsViewModel =
            loadViewModelProvider().get(NoteDetailsViewModel::class.java)

    override fun initViewModelObservers() {
        viewModel.noteLiveData().observe(this, Observer<AsyncLoad<Note>> {
            updateUi()
        })
        viewModel.deleteLiveData().observe(this, Observer<AsyncLoad<Void>> {
            updateUi()
        })
    }

    override fun onResume() {
        super.onResume()
        tryLoadNote()
    }

    fun tryDeleteNote() {
        if (viewModel.isDeleting().not()) {
            viewModel.deleteNote()
        }
    }

    private fun tryLoadNote() {
        if (viewModel.isLoading().not()) {
            viewModel.loadNote()
        }
    }

    private fun onShareClicked() {
        val context = context ?: return
        val note = viewModel.loadedNote()
        displayDialog(ShareDialog.createInstance(
                context = context,
                time = note.date.time,
                bibleTextPair = note.bibleTextPair,
                note = note.noteText))
    }

    private fun onEditClicked() {
        val activity = activity ?: return
        val note = viewModel.loadedNote()
        activity.startActivity(EditNoteActivity.createIntent(
                context = activity,
                date = note.date,
                bibleTextPair = note.bibleTextPair))
    }

    private fun onDeleteClicked() {
        showDeleteDialog()
    }

    private fun showDeleteDialog() {
        if (viewModel.isDeleting().not()) {
            displayDialog(ConfirmDeleteDialog.createInstance())
        }
    }

    private fun updateUi() {
        val context = context ?: return

        when {
            viewModel.isDeleting() -> {
                note_details_loading.loading = true
            }
            viewModel.successfullyDeleted() -> {
                activity?.supportFinishAfterTransition()
            }
            viewModel.isLoading() -> {
                note_details_loading.loading = true
            }
            else -> {
                note_details_loading.loading = false

                when {
                    viewModel.errorDeleting() -> showIndefiniteSnackbar(
                            message = R.string.note_details_error_deleting_note,
                            action = {
                                tryDeleteNote()
                            },
                            callback = {
                                viewModel.resetDeleteError()
                            }
                    )
                    viewModel.errorLoading() -> showIndefiniteSnackbar(
                            message = R.string.note_details_error_reload_note,
                            action = {
                                tryLoadNote()
                            },
                            callback = {
                                viewModel.resetLoadError()
                            }
                    )
                    else -> {
                        val note = viewModel.loadedNote()
                        note_details_losung.text = contentAsString(
                                context = context,
                                time = note.date.time,
                                bibleTextPair = note.bibleTextPair,
                                note = ""
                        )
                        note_details_note.text = note.noteText
                    }
                }
            }
        }
        activity?.invalidateOptionsMenu()
    }

}