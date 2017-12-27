package de.reiss.android.losungen.rawdata.insert

import android.content.Context
import android.support.annotation.RawRes
import de.reiss.android.losungen.database.LosungDatabaseItem
import de.reiss.android.losungen.logger.logWarnWithCrashlytics
import de.reiss.android.losungen.xmlparser.LosungXmlItem
import de.reiss.android.losungen.xmlparser.LosungXmlParser
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

abstract class XmlConverter<XmlItem : LosungXmlItem, out DatabaseItem : LosungDatabaseItem> {

    abstract val losungXmlParser: LosungXmlParser<XmlItem>

    abstract fun xmlItemToDatabaseItem(item: XmlItem, languageId: Int): DatabaseItem

    fun loadFromRaw(context: Context,
                    @RawRes resId: Int,
                    languageId: Int): List<DatabaseItem> =
            rawToString(context, resId)?.let { rawText ->
                losungXmlParser.parse(rawText).map { xmlItem ->
                    xmlItemToDatabaseItem(xmlItem, languageId)
                }
            } ?: emptyList()

    private fun rawToString(context: Context, @RawRes resId: Int): String? {
        val inputStream = context.resources.openRawResource(resId)

        val inputStreamReader = InputStreamReader(inputStream)
        val bufferedReader = BufferedReader(inputStreamReader)

        val stringBuilder = StringBuilder()
        try {
            var line = bufferedReader.readLine()
            while (line != null) {
                stringBuilder.append(line)
                stringBuilder.append('\n')
                line = bufferedReader.readLine()
            }
        } catch (e: IOException) {
            logWarnWithCrashlytics(e) { "Could not load raw file" }
            return null
        }
        return stringBuilder.toString()
    }

}