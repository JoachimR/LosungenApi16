package de.reiss.android.losungen.rawdata

import android.content.Context
import android.support.annotation.RawRes
import de.reiss.android.losungen.R
import de.reiss.android.losungen.czech
import de.reiss.android.losungen.database.DailyLosungItem
import de.reiss.android.losungen.database.DailyLosungItemDao
import de.reiss.android.losungen.database.LanguageItemDao
import de.reiss.android.losungen.hungarian
import de.reiss.android.losungen.logger.logWarnWithCrashlytics
import de.reiss.android.losungen.model.DailyLosung
import de.reiss.android.losungen.model.Language
import de.reiss.android.losungen.model.LosungContent
import de.reiss.android.losungen.preferences.AppPreferences
import de.reiss.android.losungen.util.extensions.withZeroDayTime
import de.reiss.android.losungen.xmlparser.DailyLosungXmlParser
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.concurrent.Executor
import javax.inject.Inject

open class RawToDatabaseWriter @Inject constructor(val context: Context,
                                                   val executor: Executor,
                                                   val dailyLosungItemDao: DailyLosungItemDao,
                                                   val languageItemDao: LanguageItemDao,
                                                   val appPreferences: AppPreferences) {

    companion object {

        private val rawData = HashMap<Language, Int>().apply {
            put(czech, R.raw.dailylosungen_cz_2018)
            put(hungarian, R.raw.dailylosungen_hu_2018)
        }

        private const val expectedItemsForDaily = 365
    }

    open fun tryFillDailyLosungToDatabase(language: String): Boolean {
        val key = rawData.keys.firstOrNull { it.key == language } ?: return false
        val xmlResId = rawData[key] ?: return false
        return insertDailyLosung(xmlResId, language)
    }

    private fun insertDailyLosung(@RawRes rawResId: Int, language: String): Boolean {
        val languageId = languageItemDao.find(language)?.id ?: return false

        val itemsFromRaw = loadDailyLosungFromRaw(context, rawResId, language)

        val databaseItems = itemsFromRaw.map { rawItem ->
            DailyLosungItem(
                    languageId,
                    rawItem.date.withZeroDayTime(),
                    rawItem.holiday,
                    rawItem.content.text1,
                    rawItem.content.source1,
                    rawItem.content.text2,
                    rawItem.content.source2
            )
        }

        if (databaseItems.isEmpty()) {
            logWarnWithCrashlytics {
                "Loaded daily data for language '$language' was empty"
            }
        } else {
            if (databaseItems.size != expectedItemsForDaily) {
                logWarnWithCrashlytics {
                    "Loaded daily data for language '$language'" +
                            " had ${databaseItems.size} items" +
                            " rather than $expectedItemsForDaily items"
                }
            }

            val inserted = dailyLosungItemDao.insertOrReplace(*databaseItems.toTypedArray())
            return inserted.size > 0
        }

        return false
    }

    private fun loadDailyLosungFromRaw(context: Context,
                                       @RawRes resId: Int,
                                       language: String): List<DailyLosung> =
            readRawTextFile(context, resId)?.let { rawText ->
                DailyLosungXmlParser.parseXmlItems(rawText).map {
                    DailyLosung(
                            language = language,
                            date = it.Datum,
                            holiday = it.Sonntag,
                            content = LosungContent(
                                    text1 = it.Losungstext,
                                    source1 = it.Losungsvers,
                                    text2 = it.Lehrtext,
                                    source2 = it.Lehrtextvers))
                }
            } ?: emptyList()


    private fun readRawTextFile(context: Context, @RawRes resId: Int): String? {
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