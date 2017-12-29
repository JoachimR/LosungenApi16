package de.reiss.android.losungen.main.daily.viewpager

import android.arch.lifecycle.MutableLiveData
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.database.DailyLosungItemDao
import de.reiss.android.losungen.database.LanguageItemDao
import de.reiss.android.losungen.rawdata.RawToDatabase
import de.reiss.android.losungen.util.extensions.amountOfDaysInRange
import de.reiss.android.losungen.widget.WidgetRefresher
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject

open class ViewPagerRepository @Inject constructor(private val executor: Executor,
                                                   private val dailyLosungItemDao: DailyLosungItemDao,
                                                   private val languageItemDao: LanguageItemDao,
                                                   private val rawToDatabase: RawToDatabase,
                                                   private val widgetRefresher: WidgetRefresher) {


    open fun loadItemsFor(language: String,
                          fromDate: Date,
                          toDate: Date,
                          result: MutableLiveData<AsyncLoad<String>>) {

        // set value instead of post value on purpose
        // otherwise the fragment might invoke this twice
        result.value = AsyncLoad.loading(language)

        executor.execute {

            languageItemDao.find(language)?.let { languageItem ->

                val storedItems = dailyLosungItemDao.range(languageItem.id, fromDate, toDate)

                if (storedItems != null) {
                    val expectedAmountOfDays = fromDate.amountOfDaysInRange(toDate)
                    if (storedItems.size < expectedAmountOfDays) {

                        val databaseUpdated =
                                rawToDatabase.writeRawDataToDatabase(language)

                        if (databaseUpdated) {
                            widgetRefresher.execute()
                        }
                    }
                }
            }

            // always return success, we only tried update
            result.postValue(AsyncLoad.success(language))
        }

    }

}