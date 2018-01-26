package de.reiss.android.losungen

import de.reiss.android.losungen.util.extensions.maxDayTime
import java.util.*


object DaysPositionUtil {

    const val DAYS_OF_TIME = 73413 // (Dec 31st 2100) - (Jan 1st 1900)

    private const val DURATION_ONE_DAY = 86400000 // (24h * 60m * 60s * 1000ms)

    private val FIRST_DAY_OF_TIME: Calendar = Calendar.getInstance().apply {
        set(1900, Calendar.JANUARY, 1)
    }

    @Throws(IllegalArgumentException::class)
    fun dayFor(position: Int): Calendar = Calendar.getInstance().apply {
        if (position < 0) {
            throw IllegalArgumentException("position cannot be negative")
        }
        time = FIRST_DAY_OF_TIME.time
        add(Calendar.DAY_OF_YEAR, position)
    }

    fun positionFor(calendar: Calendar) = calcPosition(calendar.time)

    private fun calcPosition(date: Date) =
            Math.round((millisSinceFirstDay(date) / DURATION_ONE_DAY).toFloat())

    private fun millisSinceFirstDay(date: Date) =
            Calendar.getInstance().apply {
                time = date
                maxDayTime()
            }.timeInMillis - FIRST_DAY_OF_TIME.timeInMillis

}
