package de.reiss.android.losungen.testutil

import de.reiss.android.losungen.model.DailyLosung
import de.reiss.android.losungen.model.LosungContent
import de.reiss.android.losungen.model.Note


fun sampleNote(number: Int) = Note(
        date = dateForNumber(number),
        noteText = "#$number note",
        losungContent = sampleLosungContent(number))

fun sampleDailyLosung(number: Int, language: String) = DailyLosung(
        language = language,
        date = dateForNumber(number),
        holiday = "#$number holiday",
        content = sampleLosungContent(number))

fun sampleLosungContent(number: Int): LosungContent {
    return LosungContent(
            "#$number text1",
            "#$number source1",
            "#$number text2",
            "#$number source2")
}
