package de.reiss.android.losungen.loader

import android.support.annotation.WorkerThread
import de.reiss.android.losungen.database.LanguageItemDao
import de.reiss.android.losungen.database.MonthlyLosungItemDao
import de.reiss.android.losungen.database.converter.Converter
import de.reiss.android.losungen.model.MonthlyLosung
import de.reiss.android.losungen.preferences.AppPreferences
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject

open class MonthlyLosungLoader @Inject constructor(private val monthlyLosungItemDao: MonthlyLosungItemDao,
                                                   private val languageItemDao: LanguageItemDao,
                                                   private val appPreferences: AppPreferences) {

    open fun loadCurrent(executor: Executor,
                         onFinished: (MonthlyLosung?) -> Unit) {
        executor.execute {
            onFinished(loadCurrent())
        }
    }

    open fun loadCurrent() =
            findLosung(date = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time)

    @WorkerThread
    private fun findLosung(date: Date): MonthlyLosung? =
            appPreferences.chosenLanguage?.let { chosenLanguage ->
                languageItemDao.find(chosenLanguage)?.let { languageItem ->
                    monthlyLosungItemDao.byDate(languageItem.id, date)?.let { losungItem ->
                        return Converter.itemToMonthlyLosung(languageItem.language, losungItem)
                    }
                }
            }

}