package de.reiss.android.losungen.widget.weekly

import android.content.Context
import android.support.annotation.WorkerThread
import de.reiss.android.losungen.R
import de.reiss.android.losungen.database.LanguageItemDao
import de.reiss.android.losungen.database.WeeklyLosungItemDao
import de.reiss.android.losungen.database.converter.Converter
import de.reiss.android.losungen.formattedWeekDate
import de.reiss.android.losungen.model.WeeklyLosung
import de.reiss.android.losungen.preferences.AppPreferences
import de.reiss.android.losungen.util.extensions.withZeroDayTime
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Named

open class WeeklyWidgetTextRefresher @Inject constructor(private val context: Context,
                                                         @Named("widget") private val executor: Executor,
                                                         private val weeklyLosungItemDao: WeeklyLosungItemDao,
                                                         private val languageItemDao: LanguageItemDao,
                                                         private val appPreferences: AppPreferences) {

    companion object {

        var currentWidgetText: String = ""

    }

    open fun execute(onFinished: () -> Unit) {
        refreshWidgetText { text ->
            WeeklyWidgetTextRefresher.currentWidgetText = text
            onFinished()
        }
    }

    private fun refreshWidgetText(onRefreshed: (String) -> Unit) {
        executor.execute {

            val losung = findLosung(startDate = Date().withZeroDayTime())
            val text =
                    if (losung == null) {
                        context.getString(R.string.no_content)
                    } else {
                        widgetText(
                                context = context,
                                weeklyLosung = losung,
                                includeDate = appPreferences.widgetShowDate()
                        )
                    }

            onRefreshed(text)
        }
    }

    @WorkerThread
    private fun findLosung(startDate: Date): WeeklyLosung? =
            appPreferences.chosenLanguage?.let { chosenLanguage ->
                languageItemDao.find(chosenLanguage)?.let { languageItem ->
                    weeklyLosungItemDao.byDate(languageItem.id, startDate)?.let { losungItem ->
                        return Converter.itemToWeeklyLosung(languageItem.language, losungItem)
                    }
                }
            }

    private fun widgetText(context: Context,
                           weeklyLosung: WeeklyLosung, includeDate: Boolean) =
            StringBuilder().apply {
                if (includeDate) {
                    append(context.getString(R.string.widget_weekly_date_range,
                            formattedWeekDate(context = context, time = weeklyLosung.startDate().time),
                            formattedWeekDate(context = context, time = weeklyLosung.endDate().time)))
                    append("<br><br>")
                }
                append(weeklyLosung.bibleText.text)
                append("<br>")
                append(weeklyLosung.bibleText.source)
            }.toString()


}