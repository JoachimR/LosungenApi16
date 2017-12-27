package de.reiss.android.losungen.rawdata

import android.content.Context
import android.support.annotation.WorkerThread
import de.reiss.android.losungen.R
import de.reiss.android.losungen.logger.logError
import de.reiss.android.losungen.logger.logInfo
import de.reiss.android.losungen.rawdata.insert.*
import de.reiss.android.losungen.util.extensions.rawIdFor
import javax.inject.Inject

open class RawToDatabase @Inject constructor(private val context: Context,
                                             private val dailyInserter: DailyXmlDatabaseInserter,
                                             private val weeklyInserter: WeeklyXmlDatabaseInserter,
                                             private val monthlyInserter: MonthlyXmlDatabaseInserter,
                                             private val yearlyInserter: YearlyXmlDatabaseInserter) {

    companion object {
        const val prefixDaily = "daily"
        const val prefixWeekly = "weekly"
        const val prefixMonthly = "monthly"
        const val prefixYearly = "yearly"
    }

    @WorkerThread
    open fun writeRawDataToDatabase(language: String): Boolean {
        var written = false
        val fileNames = findRawFileNamesFor(language)

        written = write(
                fileNames = fileNames.filter { it.startsWith(prefixDaily) },
                language = language,
                databaseInserter = dailyInserter) or written

        written = write(
                fileNames = fileNames.filter { it.startsWith(prefixWeekly) },
                language = language,
                databaseInserter = weeklyInserter) or written

        written = write(
                fileNames = fileNames.filter { it.startsWith(prefixMonthly) },
                language = language,
                databaseInserter = monthlyInserter) or written

        written = write(
                fileNames = fileNames.filter { it.startsWith(prefixYearly) },
                language = language,
                databaseInserter = yearlyInserter) or written

        return written
    }

    private fun write(fileNames: List<String>,
                      language: String,
                      databaseInserter: DatabaseInserter): Boolean {
        var written = false
        for (fileName in fileNames) {
            logInfo {
                "Trying to extract data for language $language" +
                        " from the file '$fileName'" +
                        " in order to write to sql database"
            }
            written = databaseInserter.insert(
                    language = language,
                    rawResId = context.rawIdFor(fileName)) or written
        }
        return written
    }

    private fun findRawFileNamesFor(language: String): List<String> =
            allRawFileNames().filter { it.contains(language) }

    private fun allRawFileNames(): List<String> {
        val result = mutableListOf<String>()
        for (field in R.raw::class.java.fields)
            try {
                result.add(field.name.toString())
            } catch (e: Exception) {
                logError(e) { "error when trying to read raw file name" }
            }
        return result
    }

}