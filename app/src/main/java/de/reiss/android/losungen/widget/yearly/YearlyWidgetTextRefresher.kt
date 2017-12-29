package de.reiss.android.losungen.widget.yearly

import android.content.Context
import de.reiss.android.losungen.R
import de.reiss.android.losungen.loader.YearlyLosungLoader
import de.reiss.android.losungen.model.YearlyLosung
import de.reiss.android.losungen.preferences.AppPreferences
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Named

open class YearlyWidgetTextRefresher @Inject constructor(private val context: Context,
                                                         private val appPreferences: AppPreferences,
                                                         @Named("widget") private val executor: Executor,
                                                         private val yearlyLosungLoader: YearlyLosungLoader) {

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
        yearlyLosungLoader.loadCurrent(executor = executor,
                onFinished = { losung ->
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
                })
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