package de.reiss.android.losungen.rawdata

import android.content.Context
import android.support.annotation.RawRes
import android.support.annotation.WorkerThread
import de.reiss.android.losungen.database.LanguageItemDao
import de.reiss.android.losungen.database.YearlyLosungDatabaseItem
import de.reiss.android.losungen.database.YearlyLosungItemDao
import de.reiss.android.losungen.logger.logWarnWithCrashlytics
import de.reiss.android.losungen.util.extensions.withZeroDayTime
import de.reiss.android.losungen.xmlparser.YearlyLosungXmlItem
import de.reiss.android.losungen.xmlparser.YearlyLosungXmlParser
import javax.inject.Inject

class YearlyXmlDatabaseInserter @Inject constructor(private val context: Context,
                                                    private val yearlyLosungItemDao: YearlyLosungItemDao,
                                                    private val languageItemDao: LanguageItemDao)
    : XmlConverter<YearlyLosungXmlItem, YearlyLosungDatabaseItem>(), DatabaseInserter {

    override val losungXmlParser = YearlyLosungXmlParser

    @WorkerThread
    override fun insert(language: String, @RawRes rawResId: Int): Boolean {
        val languageId = languageItemDao.find(language)?.id ?: return false
        val databaseItems = loadFromRaw(context, rawResId, languageId)

        if (databaseItems.isEmpty()) {
            logWarnWithCrashlytics {
                "Loaded yearly data for language '$language' was empty"
            }
        } else {
            val inserted = yearlyLosungItemDao.insertOrReplace(*databaseItems.toTypedArray())
            return inserted.size > 0
        }

        return false
    }

    override fun xmlItemToDatabaseItem(item: YearlyLosungXmlItem,
                                       languageId: Int): YearlyLosungDatabaseItem =
            YearlyLosungDatabaseItem(
                    languageId,
                    item.Datum.withZeroDayTime(),
                    item.Losungstext,
                    item.Losungsvers
            )

}