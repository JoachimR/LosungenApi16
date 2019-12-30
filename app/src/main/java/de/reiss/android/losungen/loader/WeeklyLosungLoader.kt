package de.reiss.android.losungen.loader

import androidx.annotation.WorkerThread
import de.reiss.android.losungen.database.LanguageItemDao
import de.reiss.android.losungen.database.WeeklyLosungItemDao
import de.reiss.android.losungen.database.converter.Converter
import de.reiss.android.losungen.model.WeeklyLosung
import de.reiss.android.losungen.preferences.AppPreferences
import de.reiss.android.losungen.util.extensions.withZeroDayTime
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject

open class WeeklyLosungLoader @Inject constructor(private val weeklyLosungItemDao: WeeklyLosungItemDao,
                                                  private val languageItemDao: LanguageItemDao,
                                                  private val appPreferences: AppPreferences) {

    open fun loadCurrent(executor: Executor,
                         onFinished: (WeeklyLosung?) -> Unit) {
        executor.execute {
            onFinished(loadCurrent())
        }
    }

    open fun loadCurrent() = findLosung(startDate = Date().withZeroDayTime())

    @WorkerThread
    private fun findLosung(startDate: Date): WeeklyLosung? =
            appPreferences.chosenLanguage?.let { chosenLanguage ->
                languageItemDao.find(chosenLanguage)?.let { languageItem ->
                    weeklyLosungItemDao.byDate(languageItem.id, startDate)?.let { losungItem ->
                        return Converter.itemToWeeklyLosung(languageItem.language, losungItem)
                    }
                }
            }

}