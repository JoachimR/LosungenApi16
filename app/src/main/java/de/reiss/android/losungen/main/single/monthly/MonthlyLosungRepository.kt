package de.reiss.android.losungen.main.single.monthly

import androidx.lifecycle.MutableLiveData
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.loader.MonthlyLosungLoader
import de.reiss.android.losungen.model.MonthlyLosung
import java.util.concurrent.Executor
import javax.inject.Inject

open class MonthlyLosungRepository @Inject constructor(private val executor: Executor,
                                                       private val monthlyLosungLoader: MonthlyLosungLoader) {

    open fun loadCurrent(result: MutableLiveData<AsyncLoad<MonthlyLosung?>>) {

        val oldData = result.value?.data
        result.value = AsyncLoad.loading(oldData)

        monthlyLosungLoader.loadCurrent(executor = executor,
                onFinished = { losung ->
                    if (losung == null) {
                        result.postValue(AsyncLoad.error(message = "Content not found"))
                    } else {
                        result.postValue(AsyncLoad.success(losung))
                    }
                })
    }

}