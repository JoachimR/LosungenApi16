package de.reiss.android.losungen.rawdata.insert

import android.content.Context
import androidx.annotation.RawRes
import androidx.annotation.WorkerThread
import de.reiss.android.losungen.database.LanguageItemDao
import de.reiss.android.losungen.database.MonthlyLosungDatabaseItem
import de.reiss.android.losungen.database.MonthlyLosungItemDao
import de.reiss.android.losungen.logger.logWarn
import de.reiss.android.losungen.util.extensions.withZeroDayTime
import de.reiss.android.losungen.xmlparser.MonthlyLosungXmlItem
import de.reiss.android.losungen.xmlparser.MonthlyLosungXmlParser
import javax.inject.Inject

class MonthlyXmlDatabaseInserter @Inject constructor(
    private val context: Context,
    private val monthlyLosungItemDao: MonthlyLosungItemDao,
    private val languageItemDao: LanguageItemDao
) : XmlConverter<MonthlyLosungXmlItem, MonthlyLosungDatabaseItem>(), DatabaseInserter {

    override val losungXmlParser = MonthlyLosungXmlParser

    @WorkerThread
    override fun insert(language: String, @RawRes rawResId: Int): Boolean {
        val languageId = languageItemDao.find(language)?.id ?: return false
        val databaseItems = loadFromRaw(context, rawResId, languageId)

        if (databaseItems.isEmpty()) {
            logWarn {
                "Loaded monthly data for language '$language' was empty"
            }
        } else {
            val inserted = monthlyLosungItemDao.insertOrReplace(*databaseItems.toTypedArray())
            return inserted.size > 0
        }

        return false
    }

    override fun xmlItemToDatabaseItem(
        item: MonthlyLosungXmlItem,
        languageId: Int
    ): MonthlyLosungDatabaseItem =
        MonthlyLosungDatabaseItem(
            languageId,
            item.Datum.withZeroDayTime(),
            item.Losungstext,
            item.Losungsvers
        )

}