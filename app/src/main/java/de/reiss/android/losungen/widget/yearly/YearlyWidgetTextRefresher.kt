package de.reiss.android.losungen.widget.yearly

import android.content.Context
import android.support.annotation.WorkerThread
import de.reiss.android.losungen.R
import de.reiss.android.losungen.database.LanguageItemDao
import de.reiss.android.losungen.database.YearlyLosungItemDao
import de.reiss.android.losungen.database.converter.Converter
import de.reiss.android.losungen.model.YearlyLosung
import de.reiss.android.losungen.preferences.AppPreferences
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Named

open class YearlyWidgetTextRefresher @Inject constructor(private val context: Context,
                                                         @Named("widget") private val executor: Executor,
                                                         private val yearlyLosungItemDao: YearlyLosungItemDao,
                                                         private val languageItemDao: LanguageItemDao,
                                                         private val appPreferences: AppPreferences) {

    companion object {

        var currentWidgetText: String = ""

    }

    open fun execute(onFinished: () -> Unit) {
        refreshWidgetText { text ->
            currentWidgetText = text
            onFinished()
        }
    }

    private fun refreshWidgetText(onRefreshed: (String) -> Unit) {
        executor.execute {

            val losung = findLosung(date = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_YEAR, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time)

            val text =
                    if (losung == null) {
                        context.getString(R.string.no_content)
                    } else {
                        widgetText(
                                yearlyLosung = losung,
                                includeDate = appPreferences.widgetShowDate()
                        )
                    }

            onRefreshed(text)
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

    private fun widgetText(yearlyLosung: YearlyLosung, includeDate: Boolean): String =
            StringBuilder().apply {
                if (includeDate) {
                    append(yearlyLosung.year)
                    append("<br><br>")
                }

                append(yearlyLosung.bibleText.text)
                append("<br>")
                append(yearlyLosung.bibleText.source)
            }.toString()


}