package de.reiss.android.losungen.note.export

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import de.reiss.android.losungen.App
import de.reiss.android.losungen.R
import de.reiss.android.losungen.architecture.AppFragmentWithSdCard
import de.reiss.android.losungen.util.extensions.onClick
import de.reiss.android.losungen.util.extensions.showShortSnackbar
import kotlinx.android.synthetic.main.note_export_fragment.*

class NoteExportFragment : AppFragmentWithSdCard<NoteExportViewModel>(R.layout.note_export_fragment) {

    companion object {

        fun createInstance() = NoteExportFragment()

    }

    override fun initViews() {
        note_export_start.onClick {
            tryExportNotes()
        }
    }

    override fun defineViewModelProvider(): ViewModelProvider =
            ViewModelProviders.of(this, NoteExportViewModel.Factory(
                    App.component.noteExportRepository))

    override fun defineViewModel(): NoteExportViewModel =
            loadViewModelProvider().get(NoteExportViewModel::class.java)

    override fun initViewModelObservers() {
        viewModel.exportLiveData().observe(this, Observer<NoteExportStatus> {
            updateUi()
        })
    }

    private fun tryExportNotes() {
        if (viewModel.isExporting().not()) {
            requestSdCardPermission()
        }
    }

    override fun onSdCardPermissionGranted() {
        if (viewModel.isExporting().not()) {
            viewModel.exportNotes()
        }
    }

    override fun onSdCardPermissionDenied() {
        showShortSnackbar(R.string.can_not_write_to_sdcard)
    }

    private fun updateUi() {
        val status = viewModel.exportLiveData().value ?: return

        val isExporting = status is ExportingStatus
        updateLoading(isExporting)

        if (isExporting.not()) {
            showShortSnackbar(
                    message = messageFor(status),
                    callback = {
                        viewModel.clearLiveData()
                    })
        }
    }

    private fun messageFor(status: NoteExportStatus) =
            when (status) {
                is NoPermissionStatus -> getString(R.string.can_not_write_to_sdcard)
                is NoNotesStatus -> getString(R.string.notes_export_no_notes)
                is ExportErrorStatus -> getString(R.string.notes_export_error,
                        status.directory, status.fileName)
                is ExportSuccessStatus -> getString(R.string.notes_export_success,
                        status.directory, status.fileName)
                else -> throw IllegalStateException("invalid status")
            }

    private fun updateLoading(loading: Boolean) {
        note_export_loading.loading = loading
        note_export_start.isEnabled = !loading
    }

}