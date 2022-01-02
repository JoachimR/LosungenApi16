package de.reiss.android.losungen.main.daily

import androidx.lifecycle.MutableLiveData
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.database.NoteItemDao
import de.reiss.android.losungen.database.converter.Converter
import de.reiss.android.losungen.loader.DailyLosungLoader
import de.reiss.android.losungen.model.DailyLosung
import de.reiss.android.losungen.model.Note
import de.reiss.android.losungen.util.extensions.withZeroDayTime
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject

open class LosungRepository @Inject constructor(
    private val executor: Executor,
    private val dailyLosungLoader: DailyLosungLoader,
    private val noteItemDao: NoteItemDao
) {


    open fun getLosungFor(date: Date, result: MutableLiveData<AsyncLoad<DailyLosung?>>) {

        val oldData = result.value?.data
        result.value = AsyncLoad.loading(oldData)

        dailyLosungLoader.loadForDate(date = date, executor = executor,
            onFinished = { losung ->
                if (losung == null) {
                    result.postValue(AsyncLoad.error(message = "Content not found"))
                } else {
                    result.postValue(AsyncLoad.success(losung))
                }
            })

    }

    open fun getNoteFor(date: Date, result: MutableLiveData<AsyncLoad<Note?>>) {

        val oldData = result.value?.data
        result.postValue(AsyncLoad.loading(oldData))

        executor.execute {
            val noteItem = noteItemDao.byDate(date.withZeroDayTime())
            result.postValue(AsyncLoad.success(Converter.itemToNote(noteItem)))
        }
    }

}