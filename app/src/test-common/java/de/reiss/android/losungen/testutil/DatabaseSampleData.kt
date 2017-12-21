package de.reiss.android.losungen.testutil

import de.reiss.android.losungen.database.DailyLosungItem
import de.reiss.android.losungen.database.LanguageItem
import de.reiss.android.losungen.database.NoteItem
import de.reiss.android.losungen.model.LosungContent

fun sampleNoteItem(number: Int) = NoteItem(
        dateForNumber(number),
        LosungContent(
                "#$number text1",
                "#$number source1",
                "#$number text2",
                "#$number source2"),
        "#$number note")

fun sampleDailyLosungItem(number: Int, languageId: Int) = DailyLosungItem(
        10 + number,
        languageId,
        dateForNumber(number),
        "#$number holiday",
        "#$number text1",
        "#$number source1",
        "#$number text2",
        "#$number source2")

fun sampleLanguageItem(languageId: Int) = LanguageItem("testLanguage", "test Language", "tst")
        .apply { id = languageId }