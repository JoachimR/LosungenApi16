package de.reiss.android.losungen.main.daily

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.architecture.AsyncLoadStatus
import de.reiss.android.losungen.model.DailyLosung
import de.reiss.android.losungen.model.Note
import java.util.*

open class LosungViewModel(private val repository: LosungRepository) : ViewModel() {

    private val losungLiveData: MutableLiveData<AsyncLoad<DailyLosung?>> = MutableLiveData()
    private val noteLiveData: MutableLiveData<AsyncLoad<Note?>> = MutableLiveData()

    open fun losungLiveData() = losungLiveData
    open fun noteLiveData() = noteLiveData

    open fun loadLosung(date: Date) {
        repository.getLosungFor(date, losungLiveData())
    }

    open fun loadNote(date: Date) {
        repository.getNoteFor(date, noteLiveData())
    }

    fun losung() = losungLiveData().value?.data
    fun note() = noteLiveData().value?.data

    fun isLoadingLosung() = losungLiveData().value?.loadStatus == AsyncLoadStatus.LOADING
    fun isErrorForLosung() = losungLiveData().value?.loadStatus == AsyncLoadStatus.ERROR
    fun isSuccessForLosung() = losungLiveData().value?.loadStatus == AsyncLoadStatus.SUCCESS

    fun isLoadingNote() = noteLiveData().value?.loadStatus == AsyncLoadStatus.LOADING

    class Factory(private val repository: LosungRepository) :
        ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            @Suppress("UNCHECKED_CAST")
            return LosungViewModel(repository) as T
        }

    }

}