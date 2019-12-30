package de.reiss.android.losungen.widget.monthly

import android.content.Context
import androidx.annotation.WorkerThread
import de.reiss.android.losungen.R
import de.reiss.android.losungen.formattedMonthDate
import de.reiss.android.losungen.loader.MonthlyLosungLoader
import de.reiss.android.losungen.model.MonthlyLosung
import de.reiss.android.losungen.preferences.AppPreferences
import javax.inject.Inject

open class MonthlyWidgetTextRefresher @Inject constructor(private val context: Context,
                                                          private val appPreferences: AppPreferences,
                                                          private val monthlyLosungLoader: MonthlyLosungLoader) {

    @WorkerThread
    open fun retrieveCurrentText(): String =
            monthlyLosungLoader.loadCurrent().let { losung ->
                if (losung == null) {
                    context.getString(R.string.no_content)
                } else {
                    widgetText(
                            context = context,
                            monthlyLosung = losung,
                            includeDate = appPreferences.widgetShowDate()
                    )
                }
            }

    private fun widgetText(context: Context, monthlyLosung: MonthlyLosung, includeDate: Boolean): String =
            StringBuilder().apply {
                if (includeDate) {
                    append(formattedMonthDate(context = context, time = monthlyLosung.startDate().time))
                    append("<br><br>")
                }

                append(monthlyLosung.bibleText.text)
                append("<br>")
                append(monthlyLosung.bibleText.source)
            }.toString()

}