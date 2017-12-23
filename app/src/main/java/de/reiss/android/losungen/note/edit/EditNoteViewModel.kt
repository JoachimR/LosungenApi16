package de.reiss.android.losungen.note.edit

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.architecture.AsyncLoadStatus
import de.reiss.android.losungen.model.BibleTextPair
import de.reiss.android.losungen.model.Note
import java.util.*

open class EditNoteViewModel(private val repository: EditNoteRepository) : ViewModel() {

    private val loadNoteLiveData: MutableLiveData<AsyncLoad<Note?>> = MutableLiveData()
    private val storeNoteLiveData: MutableLiveData<AsyncLoad<Void>> = MutableLiveData()

    open fun loadNoteLiveData() = loadNoteLiveData
    open fun storeNoteLiveData() = storeNoteLiveData

    open fun loadNote(date: Date) {
        repository.loadNote(date, loadNoteLiveData())
    }

    open fun storeNote(date: Date, text: String, bibleTextPair: BibleTextPair) {
        repository.updateNote(date, text, bibleTextPair, storeNoteLiveData())
    }

    fun note() = loadNoteLiveData().value?.data

    fun isLoadingOrStoring() = loadNoteLiveData().value?.loadStatus == AsyncLoadStatus.LOADING
            || storeNoteLiveData().value?.loadStatus == AsyncLoadStatus.LOADING

    fun loadError() = loadNoteLiveData().value?.loadStatus == AsyncLoadStatus.ERROR

    fun storeError() = storeNoteLiveData().value?.loadStatus == AsyncLoadStatus.ERROR
    fun storeSuccess() = storeNoteLiveData().value?.loadStatus == AsyncLoadStatus.SUCCESS

    fun onStoreErrorShown() {
        if (storeError()) {
            storeNoteLiveData().postValue(null)
        }
    }

    class Factory(private val repository: EditNoteRepository) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            @Suppress("UNCHECKED_CAST")
            return EditNoteViewModel(repository) as T
        }

    }

}