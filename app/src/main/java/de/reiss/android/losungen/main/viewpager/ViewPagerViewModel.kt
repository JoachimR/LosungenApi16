package de.reiss.android.losungen.main.viewpager

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.architecture.AsyncLoadStatus
import de.reiss.android.losungen.util.extensions.firstDayOfYear
import de.reiss.android.losungen.util.extensions.lastDayOfYear
import java.util.*

open class ViewPagerViewModel(initialLanguage: String,
                              private val repository: ViewPagerRepository) : ViewModel() {

    private val loadYearLiveData: MutableLiveData<AsyncLoad<String>> = MutableLiveData()

    init {
        loadYearLiveData.postValue(AsyncLoad.loading(initialLanguage))
    }

    open fun loadYearLiveData() = loadYearLiveData

    open fun prepareContentFor(language: String, date: Date) {
        repository.loadItemsFor(
                language = language,
                fromDate = date.firstDayOfYear(),
                toDate = date.lastDayOfYear(),
                result = loadYearLiveData()
        )
    }

    fun currentLanguage() = loadYearLiveData().value?.data

    fun isLoadingContent() = loadYearLiveData().value?.loadStatus == AsyncLoadStatus.LOADING

    class Factory(private val initialLanguage: String,
                  private val repository: ViewPagerRepository) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            @Suppress("UNCHECKED_CAST")
            return ViewPagerViewModel(initialLanguage, repository) as T
        }

    }

}