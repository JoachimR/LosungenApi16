package de.reiss.android.losungen.model

import java.util.*

sealed class Losung {

    abstract val language: String

    abstract fun startDate(): Date

}

data class DailyLosung(override val language: String,
                       val holiday: String,
                       val year: Int,
                       val dayOfYear: Int,
                       val bibleTextPair: BibleTextPair) : Losung() {

    override fun startDate(): Date = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.DAY_OF_YEAR, dayOfYear)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time

}

abstract class SingleTextLosung(override val language: String) : Losung() {

    abstract val bibleText: BibleText
}

data class WeeklyLosung(override val language: String,
                        val holiday: String,
                        val startYear: Int,
                        val startDayOfYear: Int,
                        val endYear: Int,
                        val endDayOfYear: Int,
                        override val bibleText: BibleText) : SingleTextLosung(language) {

    override fun startDate(): Date = Calendar.getInstance().apply {
        set(Calendar.YEAR, startYear)
        set(Calendar.DAY_OF_YEAR, startDayOfYear)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time

    fun endDate(): Date = Calendar.getInstance().apply {
        set(Calendar.YEAR, endYear)
        set(Calendar.DAY_OF_YEAR, endDayOfYear)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time

}

data class MonthlyLosung(override val language: String,
                         val year: Int,
                         val month: Int,
                         override val bibleText: BibleText) : SingleTextLosung(language) {

    override fun startDate(): Date = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month)
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time

}


data class YearlyLosung(override val language: String,
                        val year: Int,
                        override val bibleText: BibleText) : SingleTextLosung(language) {

    override fun startDate(): Date = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.DAY_OF_YEAR, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time

}