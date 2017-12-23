package de.reiss.android.losungen.testutil

import de.reiss.android.losungen.model.BibleTextPair
import de.reiss.android.losungen.model.DailyLosung
import de.reiss.android.losungen.model.Note

fun sampleNote(number: Int) = Note(
        date = dateForNumber(number),
        noteText = "#$number note",
        bibleTextPair = bibleTextPair(number))

fun sampleDailyLosung(number: Int, language: String) = DailyLosung(
        language = language,
        holiday = "#$number holiday",
        year = 2017,
        dayOfYear = number + 1,
        bibleTextPair = bibleTextPair(number))

fun bibleTextPair(number: Int): BibleTextPair {
    return BibleTextPair(
            "#$number text1",
            "#$number source1",
            "#$number text2",
            "#$number source2")
}
