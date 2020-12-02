package de.reiss.android.losungen.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.text.Html
import de.reiss.android.losungen.App
import de.reiss.android.losungen.R
import de.reiss.android.losungen.formattedDate
import de.reiss.android.losungen.model.BibleText
import de.reiss.android.losungen.model.BibleTextPair


fun shareIntent(text: String, chooserTitle: CharSequence): Intent =
        Intent.createChooser(Intent()
                .setAction(Intent.ACTION_SEND)
                .putExtra(Intent.EXTRA_TEXT, text)
                .setType("text/plain"), chooserTitle)

@Suppress("DEPRECATION")
fun htmlize(text: String) =
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(text)
        }

fun contentAsString(date: String, bibleText: BibleText) =
        StringBuilder().apply {
            append(date)
            append("\n\n")
            append(bibleText.text)
            append("\n")
            append(bibleText.source)
        }.toString()

fun contentAsString(context: Context, time: Long, bibleTextPair: BibleTextPair, note: String) =
        StringBuilder().apply {
            append(formattedDate(context, time))
            append("\n\n")

            append(bibleTextPair.first.text)
            append("\n")
            append(bibleTextPair.first.source)
            append("\n\n")

            append(bibleTextPair.second.text)
            append("\n")
            append(bibleTextPair.second.source)
            if (note.isNotEmpty()) {
                append("\n\n")
                append(note)
            }
        }.toString()

fun copyToClipboard(context: Context, text: String) {
    clipboardManager.setPrimaryClip(ClipData.newPlainText(context.getString(R.string.app_name), text))
}

private val clipboardManager: ClipboardManager by lazy {
    App.component.clipboardManager
}
