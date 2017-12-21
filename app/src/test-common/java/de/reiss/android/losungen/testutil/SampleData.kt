package de.reiss.android.losungen.testutil

import de.reiss.android.losungen.util.extensions.withZeroDayTime
import java.util.*

fun dateForNumber(number: Int): Date {
    return Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR, number)
    }.time.withZeroDayTime()
}
