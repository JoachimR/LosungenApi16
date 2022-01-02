package de.reiss.android.losungen.note.export

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import de.reiss.android.losungen.App
import de.reiss.android.losungen.R
import de.reiss.android.losungen.architecture.AppFragmentWithSdCard
import de.reiss.android.losungen.databinding.NoteExportFragmentBinding
import de.reiss.android.losungen.util.extensions.onClick
import de.reiss.android.losungen.util.extensions.showShortSnackbar

class NoteExportFragment :
    AppFragmentWithSdCard<NoteExportFragmentBinding, NoteExportViewModel>(R.layout.note_export_fragment) {

    companion object {

        fun createInstance() = NoteExportFragment()

    }

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        NoteExportFragmentBinding.inflate(inflater, container, false)

    override fun initViews() {
        binding.noteExportStart.onClick {
            tryExportNotes()
        }
    }

    override fun defineViewModelProvider(): ViewModelProvider =
        ViewModelProviders.of(
            this, NoteExportViewModel.Factory(
                App.component.noteExportRepository
            )
        )

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
            is ExportErrorStatus -> getString(
                R.string.notes_export_error,
                status.directory, status.fileName
            )
            is ExportSuccessStatus -> getString(
                R.string.notes_export_success,
                status.directory, status.fileName
            )
            else -> throw IllegalStateException("invalid status")
        }

    private fun updateLoading(loading: Boolean) {
        binding.noteExportLoading.loading = loading
        binding.noteExportStart.isEnabled = !loading
    }

}