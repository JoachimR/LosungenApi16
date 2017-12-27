package de.reiss.android.losungen

import android.content.Context
import android.text.format.DateUtils.*

fun formattedDate(context: Context, time: Long): String =
        formatDateTime(context,
                time,
                FORMAT_SHOW_DATE or FORMAT_SHOW_YEAR or FORMAT_SHOW_WEEKDAY)

fun formattedWeekDate(context: Context, time: Long): String =
        formatDateTime(context,
                time, FORMAT_SHOW_DATE)


fun formattedMonthDate(context: Context, time: Long): String =
        formatDateTime(context,
                time,
                FORMAT_NO_MONTH_DAY or FORMAT_SHOW_YEAR)