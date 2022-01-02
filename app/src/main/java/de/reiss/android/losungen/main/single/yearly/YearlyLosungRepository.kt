package de.reiss.android.losungen.main.single.yearly

import androidx.lifecycle.MutableLiveData
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.loader.YearlyLosungLoader
import de.reiss.android.losungen.model.YearlyLosung
import java.util.concurrent.Executor
import javax.inject.Inject

open class YearlyLosungRepository @Inject constructor(
    private val executor: Executor,
    private val yearlyLosungLoader: YearlyLosungLoader
) {

    open fun loadCurrent(result: MutableLiveData<AsyncLoad<YearlyLosung?>>) {

        val oldData = result.value?.data
        result.value = AsyncLoad.loading(oldData)

        yearlyLosungLoader.loadCurrent(executor = executor,
            onFinished = { losung ->
                if (losung == null) {
                    result.postValue(AsyncLoad.error(message = "Content not found"))
                } else {
                    result.postValue(AsyncLoad.success(losung))
                }
            })
    }

}