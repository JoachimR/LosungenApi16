package de.reiss.android.losungen.rawdata.insert

import android.content.Context
import androidx.annotation.RawRes
import androidx.annotation.WorkerThread
import de.reiss.android.losungen.database.DailyLosungDatabaseItem
import de.reiss.android.losungen.database.DailyLosungItemDao
import de.reiss.android.losungen.database.LanguageItemDao
import de.reiss.android.losungen.logger.logWarn
import de.reiss.android.losungen.preferences.AppPreferences
import de.reiss.android.losungen.util.extensions.withZeroDayTime
import de.reiss.android.losungen.xmlparser.DailyLosungXmlItem
import de.reiss.android.losungen.xmlparser.DailyLosungXmlParser
import java.util.concurrent.Executor
import javax.inject.Inject

class DailyXmlDatabaseInserter @Inject constructor(val context: Context,
                                                   val executor: Executor,
                                                   val dailyLosungItemDao: DailyLosungItemDao,
                                                   val languageItemDao: LanguageItemDao,
                                                   val appPreferences: AppPreferences)
    : XmlConverter<DailyLosungXmlItem, DailyLosungDatabaseItem>(), DatabaseInserter {

    companion object {

        private const val expectedItemsForDaily = 365

    }

    override val losungXmlParser = DailyLosungXmlParser

    @WorkerThread
    override fun insert(language: String, @RawRes rawResId: Int): Boolean {
        val languageId = languageItemDao.find(language)?.id ?: return false
        val databaseItems = loadFromRaw(context, rawResId, languageId)

        if (databaseItems.isEmpty()) {
            logWarn {
                "Loaded daily data for language '$language' was empty"
            }
        } else {
            if (databaseItems.size != expectedItemsForDaily) {
                logWarn {
                    "Loaded daily data for language '$language'" +
                            " had ${databaseItems.size} items" +
                            " rather than ${expectedItemsForDaily} items"
                }
            }

            val inserted = dailyLosungItemDao.insertOrReplace(*databaseItems.toTypedArray())
            return inserted.size > 0
        }

        return false
    }

    override fun xmlItemToDatabaseItem(item: DailyLosungXmlItem,
                                       languageId: Int): DailyLosungDatabaseItem =
            DailyLosungDatabaseItem(
                    languageId,
                    item.Datum.withZeroDayTime(),
                    item.Sonntag,
                    item.Losungstext,
                    item.Losungsvers,
                    item.Lehrtext,
                    item.Lehrtextvers
            )

}