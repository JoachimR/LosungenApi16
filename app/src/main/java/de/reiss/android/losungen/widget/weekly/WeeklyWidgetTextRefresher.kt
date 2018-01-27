package de.reiss.android.losungen.widget.weekly

import android.content.Context
import de.reiss.android.losungen.R
import de.reiss.android.losungen.formattedWeekDate
import de.reiss.android.losungen.loader.WeeklyLosungLoader
import de.reiss.android.losungen.model.WeeklyLosung
import de.reiss.android.losungen.preferences.AppPreferences
import javax.inject.Inject

open class WeeklyWidgetTextRefresher @Inject constructor(private val context: Context,
                                                         private val appPreferences: AppPreferences,
                                                         private val weeklyLosungLoader: WeeklyLosungLoader) {

    open fun retrieveCurrentText(): String =
            weeklyLosungLoader.loadCurrent().let { losung ->
                if (losung == null) {
                    context.getString(R.string.no_content)
                } else {
                    widgetText(
                            context = context,
                            weeklyLosung = losung,
                            includeDate = appPreferences.widgetShowDate()
                    )
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