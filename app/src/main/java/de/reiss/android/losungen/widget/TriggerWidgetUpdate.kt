package de.reiss.android.losungen.widget

import de.reiss.android.losungen.widget.daily.DailyWidgetProvider
import de.reiss.android.losungen.widget.monthly.MonthlyWidgetProvider
import de.reiss.android.losungen.widget.weekly.WeeklyWidgetProvider
import de.reiss.android.losungen.widget.yearly.YearlyWidgetProvider

fun triggerWidgetUpdate() {
    DailyWidgetProvider.refresh()
    WeeklyWidgetProvider.refresh()
    MonthlyWidgetProvider.refresh()
    YearlyWidgetProvider.refresh()
}