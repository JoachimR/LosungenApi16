package de.reiss.android.losungen.main.content

import android.arch.lifecycle.MutableLiveData
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.database.DailyLosungItemDao
import de.reiss.android.losungen.database.LanguageItemDao
import de.reiss.android.losungen.database.NoteItemDao
import de.reiss.android.losungen.database.converter.Converter
import de.reiss.android.losungen.model.DailyLosung
import de.reiss.android.losungen.model.Note
import de.reiss.android.losungen.preferences.AppPreferences
import de.reiss.android.losungen.util.extensions.withZeroDayTime
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject

open class LosungRepository @Inject constructor(private val executor: Executor,
                                                private val dailyLosungItemDao: DailyLosungItemDao,
                                                private val languageItemDao: LanguageItemDao,
                                                private val noteItemDao: NoteItemDao,
                                                private val appPreferences: AppPreferences) {

    open fun getLosungFor(date: Date, result: MutableLiveData<AsyncLoad<DailyLosung>>) {

        val oldData = result.value?.data
        result.value = AsyncLoad.loading(oldData)

        executor.execute {
            val languageItem = languageItemDao.find(appPreferences.chosenLanguage)
                    ?: throw IllegalStateException("Unknown language")

            val fromDatabase = dailyLosungItemDao.byDate(languageItem.id, date.withZeroDayTime())
            if (fromDatabase == null) {
                result.postValue(AsyncLoad.error(message = "Content not found"))
            } else {
                result.postValue(AsyncLoad.success(
                        Converter.itemToDailyLosung(languageItem.language, fromDatabase)!!))
            }

        }
    }

    open fun getNoteFor(date: Date, result: MutableLiveData<AsyncLoad<Note>>) {

        val oldData = result.value?.data
        result.postValue(AsyncLoad.loading(oldData))

        executor.execute {
            val noteItem = noteItemDao.byDate(date.withZeroDayTime())
            result.postValue(AsyncLoad.success(Converter.itemToNote(noteItem)))
        }
    }

}