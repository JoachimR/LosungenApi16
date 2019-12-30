package de.reiss.android.losungen.preferences

import androidx.lifecycle.MutableLiveData
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.database.LanguageItemDao
import de.reiss.android.losungen.model.Language
import java.util.concurrent.Executor
import javax.inject.Inject

class AppPreferencesRepository @Inject constructor(private val executor: Executor,
                                                   private val languageItemDao: LanguageItemDao) {

    fun loadLanguageItems(result: MutableLiveData<AsyncLoad<List<Language>>>) {
        val oldData = result.value?.data
        result.postValue(AsyncLoad.loading(oldData))

        executor.execute {
            val allData = languageItemDao.all().map {
                Language(it.language, it.name, it.languageCode)
            }
            result.postValue(AsyncLoad.success(allData))
        }
    }

}