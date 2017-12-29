package de.reiss.android.losungen.loader

import android.support.annotation.WorkerThread
import de.reiss.android.losungen.database.DailyLosungItemDao
import de.reiss.android.losungen.database.LanguageItemDao
import de.reiss.android.losungen.database.converter.Converter
import de.reiss.android.losungen.model.DailyLosung
import de.reiss.android.losungen.preferences.AppPreferences
import de.reiss.android.losungen.util.extensions.withZeroDayTime
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject

open class DailyLosungLoader @Inject constructor(private val dailyLosungItemDao: DailyLosungItemDao,
                                                 private val languageItemDao: LanguageItemDao,
                                                 private val appPreferences: AppPreferences) {

    open fun loadForDate(date: Date,
                         executor: Executor,
                         onFinished: (DailyLosung?) -> Unit) {
        executor.execute {

            val losung = findLosung(date.withZeroDayTime())

            onFinished(losung)
        }
    }

    open fun loadCurrent(executor: Executor,
                         onFinished: (DailyLosung?) -> Unit) {
        executor.execute {

            val losung = findLosung(Date().withZeroDayTime())

            onFinished(losung)
        }
    }

    @WorkerThread
    private fun findLosung(date: Date): DailyLosung? =
            appPreferences.chosenLanguage?.let { chosenLanguage ->
                languageItemDao.find(chosenLanguage)?.let { languageItem ->
                    dailyLosungItemDao.byDate(languageItem.id, date)?.let { losungItem ->
                        return Converter.itemToDailyLosung(languageItem.language, losungItem)
                    }
                }
            }

}