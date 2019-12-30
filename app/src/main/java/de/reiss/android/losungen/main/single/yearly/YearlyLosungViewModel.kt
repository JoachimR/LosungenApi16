package de.reiss.android.losungen.main.single.yearly

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.architecture.AsyncLoadStatus
import de.reiss.android.losungen.model.YearlyLosung

open class YearlyLosungViewModel(private val repository: YearlyLosungRepository) : ViewModel() {

    private val losungLiveData: MutableLiveData<AsyncLoad<YearlyLosung?>> = MutableLiveData()

    open fun losungLiveData() = losungLiveData

    open fun loadCurrent() {
        repository.loadCurrent(losungLiveData())
    }

    fun losung() = losungLiveData().value?.data

    fun isLoading() = losungLiveData().value?.loadStatus == AsyncLoadStatus.LOADING
    fun isError() = losungLiveData().value?.loadStatus == AsyncLoadStatus.ERROR
    fun isSuccess() = losungLiveData().value?.loadStatus == AsyncLoadStatus.SUCCESS

    class Factory(private val repository: YearlyLosungRepository) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            @Suppress("UNCHECKED_CAST")
            return YearlyLosungViewModel(repository) as T
        }

    }

}