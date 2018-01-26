package de.reiss.android.losungen.widget.yearly

import android.content.Context
import de.reiss.android.losungen.R
import de.reiss.android.losungen.loader.YearlyLosungLoader
import de.reiss.android.losungen.model.YearlyLosung
import de.reiss.android.losungen.preferences.AppPreferences
import javax.inject.Inject

open class YearlyWidgetTextRefresher @Inject constructor(private val context: Context,
                                                         private val appPreferences: AppPreferences,
                                                         private val yearlyLosungLoader: YearlyLosungLoader) {

    open fun retrieveCurrentText(): String =
            yearlyLosungLoader.loadCurrent().let { losung ->
                if (losung == null) {
                    context.getString(R.string.no_content)
                } else {
                    widgetText(
                            yearlyLosung = losung,
                            includeDate = appPreferences.widgetShowDate()
                    )
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