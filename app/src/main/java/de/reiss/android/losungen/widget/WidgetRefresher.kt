package de.reiss.android.losungen.widget

import android.content.Context
import android.support.annotation.WorkerThread
import de.reiss.android.losungen.R
import de.reiss.android.losungen.database.DailyLosungItemDao
import de.reiss.android.losungen.database.LanguageItemDao
import de.reiss.android.losungen.database.converter.Converter
import de.reiss.android.losungen.formattedDate
import de.reiss.android.losungen.model.DailyLosung
import de.reiss.android.losungen.preferences.AppPreferences
import de.reiss.android.losungen.util.extensions.withZeroDayTime
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Named

open class WidgetRefresher @Inject constructor(private val context: Context,
                                               @Named("widget") private val executor: Executor,
                                               private val dailyLosungItemDao: DailyLosungItemDao,
                                               private val languageItemDao: LanguageItemDao,
                                               private val appPreferences: AppPreferences) {

    companion object {

        var currentWidgetText: String = ""

    }

    open fun execute() {
        refreshWidgetText { text ->
            currentWidgetText = text
            WidgetProvider.triggerWidgetRefresh()
        }
    }

    private fun refreshWidgetText(onRefreshed: (String) -> Unit) {
        executor.execute {

            val losung = findLosung(Date().withZeroDayTime())
            val text =
                    if (losung == null) {
                        context.getString(R.string.no_content)
                    } else {
                        widgetText(
                                context = context,
                                dailyLosung = losung,
                                includeDate = appPreferences.widgetShowDate()
                        )
                    }

            onRefreshed(text)
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

    private fun widgetText(context: Context, dailyLosung: DailyLosung, includeDate: Boolean): String =
            StringBuilder().apply {
                if (includeDate) {
                    append(formattedDate(context = context, time = dailyLosung.date.time))
                    append("<br><br>")
                }

                append(dailyLosung.content.text1)
                append("<br>")
                append(dailyLosung.content.source1)
                append("<br><br>")

                append(dailyLosung.content.text2)
                append("<br>")
                append(dailyLosung.content.source2)
            }.toString()


}