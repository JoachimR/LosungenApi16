package de.reiss.android.losungen.rawdata.insert

import android.content.Context
import androidx.annotation.RawRes
import androidx.annotation.WorkerThread
import de.reiss.android.losungen.database.LanguageItemDao
import de.reiss.android.losungen.database.WeeklyLosungDatabaseItem
import de.reiss.android.losungen.database.WeeklyLosungItemDao
import de.reiss.android.losungen.logger.logWarn
import de.reiss.android.losungen.util.extensions.withZeroDayTime
import de.reiss.android.losungen.xmlparser.WeeklyLosungXmlItem
import de.reiss.android.losungen.xmlparser.WeeklyLosungXmlParser
import javax.inject.Inject

class WeeklyXmlDatabaseInserter @Inject constructor(
    private val context: Context,
    private val weeklyLosungItemDao: WeeklyLosungItemDao,
    private val languageItemDao: LanguageItemDao
) : XmlConverter<WeeklyLosungXmlItem, WeeklyLosungDatabaseItem>(), DatabaseInserter {

    override val losungXmlParser = WeeklyLosungXmlParser

    @WorkerThread
    override fun insert(language: String, @RawRes rawResId: Int): Boolean {
        val languageId = languageItemDao.find(language)?.id ?: return false
        val databaseItems = loadFromRaw(context, rawResId, languageId)

        if (databaseItems.isEmpty()) {
            logWarn {
                "Loaded weekly data for language '$language' was empty"
            }
        } else {
            val inserted = weeklyLosungItemDao.insertOrReplace(*databaseItems.toTypedArray())
            return inserted.size > 0
        }

        return false
    }

    override fun xmlItemToDatabaseItem(
        item: WeeklyLosungXmlItem,
        languageId: Int
    ): WeeklyLosungDatabaseItem =
        WeeklyLosungDatabaseItem(
            languageId,
            item.StartDatum.withZeroDayTime(),
            item.EndDatum.withZeroDayTime(),
            item.Sonntag,
            item.Losungstext,
            item.Losungsvers
        )

}