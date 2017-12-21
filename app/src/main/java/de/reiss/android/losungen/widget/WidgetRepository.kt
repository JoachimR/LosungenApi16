package de.reiss.android.losungen.widget

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
import javax.inject.Named

class WidgetRepository @Inject constructor(@Named("widget") private val executor: Executor,
                                           private val dailyLosungItemDao: DailyLosungItemDao,
                                           private val languageItemDao: LanguageItemDao,
                                           private val appPreferences: AppPreferences) {

    fun loadLosungForToday(onLosungLoaded: (DailyLosung?) -> Unit) {
        executor.execute {
            onLosungLoaded(findLosung(Date().withZeroDayTime()))
        }
    }

    @WorkerThread
    private fun findLosung(date: Date): DailyLosung? =
            appPreferences.chosenLanguage?.let { chosenLanguage ->
                languageItemDao.find(chosenLanguage)?.let { languageItem ->
                    dailyLosungItemDao.byDate(languageItem.id, date)?.let { item ->
                        return Converter.itemToDailyLosung(languageItem.language, item)
                    }
                }
            }

}