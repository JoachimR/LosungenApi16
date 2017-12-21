package de.reiss.android.losungen

import android.content.Context
import android.text.format.DateUtils.*
import de.reiss.android.losungen.model.Language


val czech = Language(key = "losungen_cz", name = "český", languageCode = "cs")
val hungarian = Language(key = "losungen_hu", name = "maďarský", languageCode = "hu")

val allLanguages = listOf(czech, hungarian)

fun formattedDate(context: Context, time: Long): String =
        formatDateTime(context,
                time,
                FORMAT_SHOW_DATE or FORMAT_SHOW_YEAR or FORMAT_SHOW_WEEKDAY)
