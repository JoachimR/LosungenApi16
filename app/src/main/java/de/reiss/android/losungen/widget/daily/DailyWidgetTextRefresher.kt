package de.reiss.android.losungen.widget.daily

import android.content.Context
import de.reiss.android.losungen.R
import de.reiss.android.losungen.formattedDate
import de.reiss.android.losungen.loader.DailyLosungLoader
import de.reiss.android.losungen.model.DailyLosung
import de.reiss.android.losungen.preferences.AppPreferences
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Named

open class DailyWidgetTextRefresher @Inject constructor(private val context: Context,
                                                        private val appPreferences: AppPreferences,
                                                        @Named("widget") private val executor: Executor,
                                                        private val dailyLosungLoader: DailyLosungLoader) {

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
        dailyLosungLoader.loadCurrent(executor = executor,
                onFinished = { losung ->
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
                })
    }

    private fun widgetText(context: Context, dailyLosung: DailyLosung, includeDate: Boolean): String =
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