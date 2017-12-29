package de.reiss.android.losungen.loader

import android.support.annotation.WorkerThread
import de.reiss.android.losungen.database.LanguageItemDao
import de.reiss.android.losungen.database.YearlyLosungItemDao
import de.reiss.android.losungen.database.converter.Converter
import de.reiss.android.losungen.model.YearlyLosung
import de.reiss.android.losungen.preferences.AppPreferences
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject

open class YearlyLosungLoader @Inject constructor(private val yearlyLosungItemDao: YearlyLosungItemDao,
                                                  private val languageItemDao: LanguageItemDao,
                                                  private val appPreferences: AppPreferences) {

    open fun loadCurrent(executor: Executor,
                         onFinished: (YearlyLosung?) -> Unit) {
        executor.execute {

            val losung = findLosung(date = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_YEAR, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time)

            onFinished(losung)
        }
    }

    @WorkerThread
    private fun findLosung(date: Date): YearlyLosung? =
            appPreferences.chosenLanguage?.let { chosenLanguage ->
                languageItemDao.find(chosenLanguage)?.let { languageItem ->
                    yearlyLosungItemDao.byDate(languageItem.id, date)?.let { losungItem ->
                        return Converter.itemToYearlyLosung(languageItem.language, losungItem)
                    }
                }
            }

}