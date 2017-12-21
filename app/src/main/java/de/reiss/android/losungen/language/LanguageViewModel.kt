package de.reiss.android.losungen.language

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.architecture.AsyncLoadStatus
import de.reiss.android.losungen.model.Language

class LanguageViewModel(private val repository: LanguageRepository) : ViewModel() {

    var languagesLiveData: MutableLiveData<AsyncLoad<List<Language>>> = MutableLiveData()

    fun languages() = languagesLiveData.value?.data ?: emptyList()

    fun refreshLanguages() {
        repository.loadLanguages(languagesLiveData)
    }

    fun isLoadingLanguages() = languagesLiveData.value?.loadStatus == AsyncLoadStatus.LOADING

    class Factory(private val repository: LanguageRepository) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            @Suppress("UNCHECKED_CAST")
            return LanguageViewModel(repository) as T
        }

    }

}