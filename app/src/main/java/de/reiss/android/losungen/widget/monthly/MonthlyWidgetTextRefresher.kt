package de.reiss.android.losungen.widget.monthly

import android.content.Context
import de.reiss.android.losungen.R
import de.reiss.android.losungen.formattedMonthDate
import de.reiss.android.losungen.loader.MonthlyLosungLoader
import de.reiss.android.losungen.model.MonthlyLosung
import de.reiss.android.losungen.preferences.AppPreferences
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Named

open class MonthlyWidgetTextRefresher @Inject constructor(private val context: Context,
                                                          private val appPreferences: AppPreferences,
                                                          @Named("widget") private val executor: Executor,
                                                          private val monthlyLosungLoader: MonthlyLosungLoader) {

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
        monthlyLosungLoader.loadCurrent(executor = executor,
                onFinished = { losung ->
                    val text =
                            if (losung == null) {
                                context.getString(R.string.no_content)
                            } else {
                                widgetText(
                                        context = context,
                                        monthlyLosung = losung,
                                        includeDate = appPreferences.widgetShowDate()
                                )
                            }

                    onRefreshed(text)
                })
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