package de.reiss.android.losungen.testutil

import de.reiss.android.losungen.database.DailyLosungDatabaseItem
import de.reiss.android.losungen.database.LanguageItem
import de.reiss.android.losungen.database.NoteItem
import de.reiss.android.losungen.model.BibleTextPair

fun sampleNoteItem(number: Int) = NoteItem(
        dateForNumber(number),
        BibleTextPair(
                "#$number text1",
                "#$number source1",
                "#$number text2",
                "#$number source2"),
        "#$number note")

fun sampleDailyLosungItem(number: Int, languageId: Int) = DailyLosungDatabaseItem(
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