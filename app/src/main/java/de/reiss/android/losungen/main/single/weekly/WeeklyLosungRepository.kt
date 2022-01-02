package de.reiss.android.losungen.main.single.weekly

import androidx.lifecycle.MutableLiveData
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.loader.WeeklyLosungLoader
import de.reiss.android.losungen.model.WeeklyLosung
import java.util.concurrent.Executor
import javax.inject.Inject

open class WeeklyLosungRepository @Inject constructor(
    private val executor: Executor,
    private val weeklyLosungLoader: WeeklyLosungLoader
) {

    open fun loadCurrent(result: MutableLiveData<AsyncLoad<WeeklyLosung?>>) {

        val oldData = result.value?.data
        result.value = AsyncLoad.loading(oldData)

        weeklyLosungLoader.loadCurrent(executor = executor,
            onFinished = { losung ->
                if (losung == null) {
                    result.postValue(AsyncLoad.error(message = "Content not found"))
                } else {
                    result.postValue(AsyncLoad.success(losung))
                }
            })
    }

}