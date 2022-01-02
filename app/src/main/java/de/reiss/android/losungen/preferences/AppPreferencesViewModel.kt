package de.reiss.android.losungen.preferences

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.architecture.AsyncLoadStatus
import de.reiss.android.losungen.model.Language

class AppPreferencesViewModel(private val repository: AppPreferencesRepository) : ViewModel() {

    var languagesLiveData: MutableLiveData<AsyncLoad<List<Language>>> = MutableLiveData()

    fun languages() = languagesLiveData.value?.data ?: emptyList()

    fun loadLanguages() {
        repository.loadLanguageItems(languagesLiveData)
    }

    fun isLoadingLanguages() = languagesLiveData.value?.loadStatus == AsyncLoadStatus.LOADING

    class Factory(private val repository: AppPreferencesRepository) :
        ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            @Suppress("UNCHECKED_CAST")
            return AppPreferencesViewModel(repository) as T
        }

    }

}