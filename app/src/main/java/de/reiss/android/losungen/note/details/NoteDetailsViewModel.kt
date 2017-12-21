package de.reiss.android.losungen.note.details

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.architecture.AsyncLoadStatus.*
import de.reiss.android.losungen.model.Note

open class NoteDetailsViewModel(private val initialNote: Note,
                                private val repository: NoteDetailsRepository) : ViewModel() {

    private val noteLiveData: MutableLiveData<AsyncLoad<Note>> = MutableLiveData()

    private val deleteLiveData: MutableLiveData<AsyncLoad<Void>> = MutableLiveData()

    init {
        noteLiveData.value = AsyncLoad.success(initialNote)
    }

    open fun noteLiveData() = noteLiveData

    open fun deleteLiveData() = deleteLiveData

    open fun loadNote() {
        repository.loadNote(loadedNote(), noteLiveData())
    }

    open fun deleteNote() {
        repository.deleteNote(loadedNote(), deleteLiveData())
    }

    fun isLoading() = noteLiveData().value?.loadStatus == LOADING
    fun errorLoading() = noteLiveData().value?.loadStatus == ERROR
    fun loadedNote() = noteLiveData().value?.data ?: initialNote

    fun isDeleting() = deleteLiveData().value?.loadStatus == LOADING
    fun successfullyDeleted() = deleteLiveData().value?.loadStatus == SUCCESS
    fun errorDeleting() = deleteLiveData().value?.loadStatus == ERROR

    fun resetLoadError() {
        if (errorLoading()) {
            val oldNote = loadedNote()
            noteLiveData().value = AsyncLoad.success(oldNote)
        }
    }

    fun resetDeleteError() {
        if (errorDeleting()) {
            deleteLiveData().value = null
        }
    }

    class Factory(private val note: Note,
                  private val repository: NoteDetailsRepository) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            @Suppress("UNCHECKED_CAST")
            return NoteDetailsViewModel(note, repository) as T
        }

    }

}