package de.reiss.android.losungen.rawdata

import android.support.annotation.WorkerThread
import de.reiss.android.losungen.R
import de.reiss.android.losungen.czech
import de.reiss.android.losungen.hungarian
import de.reiss.android.losungen.model.Language
import java.util.*
import javax.inject.Inject

open class RawToDatabaseWriter @Inject constructor(private val dailyInserter: DailyXmlDatabaseInserter,
                                                   private val weeklyInserter: WeeklyXmlDatabaseInserter,
                                                   private val monthlyInserter: MonthlyXmlDatabaseInserter,
                                                   private val yearlyInserter: YearlyXmlDatabaseInserter) {

    companion object {

        val dailyRawData = HashMap<Language, Int>().apply {
            put(czech, R.raw.dailylosungen_cz_2018)
            put(hungarian, R.raw.dailylosungen_hu_2018)
        }

        val weeklyRawData = HashMap<Language, Int>().apply {
            put(czech, R.raw.weeklylosungen_cz_2018)
            put(hungarian, R.raw.weeklylosungen_hu_2018)
        }

        val monthlyRawData = HashMap<Language, Int>().apply {
            put(czech, R.raw.monthlylosungen_cz_2018)
            put(hungarian, R.raw.monthlylosungen_hu_2018)
        }

        val yearlyRawData = HashMap<Language, Int>().apply {
            put(czech, R.raw.yearlylosungen_cz_2018)
            put(hungarian, R.raw.yearlylosungen_hu_2018)
        }

    }

    @WorkerThread
    open fun writeRawDataToDatabase(language: String): Boolean {
        var written = writeRaw(dailyInserter, dailyRawData, language)
        written = writeRaw(weeklyInserter, weeklyRawData, language) or written
        written = writeRaw(monthlyInserter, monthlyRawData, language) or written
        written = writeRaw(yearlyInserter, yearlyRawData, language) or written
        return written
    }

    private fun writeRaw(databaseInserter: DatabaseInserter,
                         rawData: HashMap<Language, Int>,
                         language: String): Boolean {
        var written = false
        val keys = rawData.keys.filter { it.key == language }
        for (key in keys) {
            rawData[key]?.let { xmlResId ->
                written = databaseInserter.insert(language, xmlResId) or written
            }
        }
        return written
    }

}