package de.reiss.android.losungen.database.converter


import de.reiss.android.losungen.database.*
import de.reiss.android.losungen.model.*
import java.util.*

object Converter {

    fun itemToDailyLosung(
        language: String,
        item: DailyLosungDatabaseItem?
    ) =
        item?.let {
            val calendar = Calendar.getInstance().apply {
                timeInMillis = it.date.time
            }
            DailyLosung(
                language = language,
                holiday = it.holiday,
                year = calendar.get(Calendar.YEAR),
                dayOfYear = calendar.get(Calendar.DAY_OF_YEAR),
                bibleTextPair = BibleTextPair(
                    firstText = it.text1,
                    firstSource = it.source1,
                    secondText = it.text2,
                    secondSource = it.source2
                )
            )
        }

    fun itemToWeeklyLosung(
        language: String,
        item: WeeklyLosungDatabaseItem?
    ) =
        item?.let {
            val startCalendar = Calendar.getInstance().apply {
                timeInMillis = it.startdate.time
            }
            val endCalendar = Calendar.getInstance().apply {
                timeInMillis = it.enddate.time
            }
            WeeklyLosung(
                language = language,
                holiday = it.holiday,
                startYear = startCalendar.get(Calendar.YEAR),
                startDayOfYear = startCalendar.get(Calendar.DAY_OF_YEAR),
                endYear = endCalendar.get(Calendar.YEAR),
                endDayOfYear = endCalendar.get(Calendar.DAY_OF_YEAR),
                bibleText = BibleText(
                    text = it.text,
                    source = it.source
                )
            )
        }


    fun itemToMonthlyLosung(
        language: String,
        item: MonthlyLosungDatabaseItem?
    ) =
        item?.let {
            val startCalendar = Calendar.getInstance().apply {
                timeInMillis = it.date.time
            }
            MonthlyLosung(
                language = language,
                year = startCalendar.get(Calendar.YEAR),
                month = startCalendar.get(Calendar.MONTH),
                bibleText = BibleText(
                    text = it.text,
                    source = it.source
                )
            )
        }

    fun itemToYearlyLosung(
        language: String,
        item: YearlyLosungDatabaseItem?
    ) =
        item?.let {
            YearlyLosung(
                language = language,
                year = Calendar.getInstance().apply {
                    timeInMillis = it.date.time
                }.get(Calendar.YEAR),
                bibleText = BibleText(
                    text = it.text,
                    source = it.source
                )
            )
        }

    fun itemToNote(item: NoteItem?) =
        item?.let {
            Note(
                date = it.date,
                noteText = it.note,
                bibleTextPair = BibleTextPair(
                    firstText = it.text1,
                    firstSource = it.source1,
                    secondText = it.text2,
                    secondSource = it.source2
                )
            )
        }

}