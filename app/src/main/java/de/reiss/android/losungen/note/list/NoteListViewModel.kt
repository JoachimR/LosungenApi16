package de.reiss.android.losungen.note.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.architecture.AsyncLoadStatus.LOADING

open class NoteListViewModel(private val repository: NoteListRepository) : ViewModel() {

    private val notesLiveData: MutableLiveData<AsyncLoad<FilteredNotes>> = MutableLiveData()

    init {
        notesLiveData.value = AsyncLoad.success(FilteredNotes())
    }

    open fun notesLiveData() = notesLiveData

    open fun loadNotes() {
        repository.getAllNotes(notesLiveData())
    }

    open fun applyNewFilter(query: String) {
        repository.applyNewFilter(query, notesLiveData())
    }

    fun notes() = notesLiveData().value?.data ?: FilteredNotes(emptyList(), emptyList(), "")

    fun isLoadingNotes() = notesLiveData().value?.loadStatus == LOADING

    class Factory(private val repository: NoteListRepository) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            @Suppress("UNCHECKED_CAST")
            return NoteListViewModel(repository) as T
        }

    }

}