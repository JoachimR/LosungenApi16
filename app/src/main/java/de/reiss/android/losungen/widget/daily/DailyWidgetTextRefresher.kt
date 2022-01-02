package de.reiss.android.losungen.widget.daily

import android.content.Context
import androidx.annotation.WorkerThread
import de.reiss.android.losungen.R
import de.reiss.android.losungen.formattedDate
import de.reiss.android.losungen.loader.DailyLosungLoader
import de.reiss.android.losungen.model.DailyLosung
import de.reiss.android.losungen.preferences.AppPreferences
import javax.inject.Inject

open class DailyWidgetTextRefresher @Inject constructor(
    private val context: Context,
    private val appPreferences: AppPreferences,
    private val dailyLosungLoader: DailyLosungLoader
) {

    @WorkerThread
    open fun retrieveCurrentText(): String =
        dailyLosungLoader.loadCurrent().let { losung ->
            if (losung == null) {
                context.getString(R.string.no_content)
            } else {
                widgetText(
                    context = context,
                    dailyLosung = losung,
                    includeDate = appPreferences.widgetShowDate()
                )
            }
        }

    private fun widgetText(
        context: Context,
        dailyLosung: DailyLosung,
        includeDate: Boolean
    ): String =
        StringBuilder().apply {
            if (includeDate) {
                append(formattedDate(context = context, time = dailyLosung.startDate().time))
                append("<br><br>")
            }

            append(dailyLosung.bibleTextPair.first.text)
            append("<br>")
            append(dailyLosung.bibleTextPair.first.source)
            append("<br><br>")

            append(dailyLosung.bibleTextPair.second.text)
            append("<br>")
            append(dailyLosung.bibleTextPair.second.source)
        }.toString()

}