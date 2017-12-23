package de.reiss.android.losungen.widget

import de.reiss.android.losungen.widget.daily.DailyWidgetProvider
import de.reiss.android.losungen.widget.daily.DailyWidgetTextRefresher
import de.reiss.android.losungen.widget.monthly.MonthlyWidgetProvider
import de.reiss.android.losungen.widget.monthly.MonthlyWidgetTextRefresher
import de.reiss.android.losungen.widget.weekly.WeeklyWidgetProvider
import de.reiss.android.losungen.widget.weekly.WeeklyWidgetTextRefresher
import de.reiss.android.losungen.widget.yearly.YearlyWidgetProvider
import de.reiss.android.losungen.widget.yearly.YearlyWidgetTextRefresher
import javax.inject.Inject

open class WidgetRefresher @Inject constructor(
        private val dailyWidgetTextRefresher: DailyWidgetTextRefresher,
        private val weeklyWidgetTextRefresher: WeeklyWidgetTextRefresher,
        private val monthlyWidgetTextRefresher: MonthlyWidgetTextRefresher,
        private val yearlyWidgetTextRefresher: YearlyWidgetTextRefresher) {


    open fun execute() {
        dailyWidgetTextRefresher.execute {
            weeklyWidgetTextRefresher.execute {
                monthlyWidgetTextRefresher.execute {
                    yearlyWidgetTextRefresher.execute {

                        DailyWidgetProvider.triggerWidgetRefresh()
                        WeeklyWidgetProvider.triggerWidgetRefresh()
                        MonthlyWidgetProvider.triggerWidgetRefresh()
                        YearlyWidgetProvider.triggerWidgetRefresh()
                    }
                }
            }
        }
    }

}