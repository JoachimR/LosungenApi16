package de.reiss.android.losungen.widget.monthly

import android.content.Context
import android.support.annotation.WorkerThread
import de.reiss.android.losungen.App
import de.reiss.android.losungen.widget.WidgetRemoteViewsFactory

class MonthlyWidgetRemoteViewsFactory(context: Context) : WidgetRemoteViewsFactory(context) {

    private val monthlyWidgetTextRefresher: MonthlyWidgetTextRefresher by lazy {
        App.component.monthlyWidgetTextRefresher
    }

    @WorkerThread
    override fun loadWidgetText(): String = monthlyWidgetTextRefresher.retrieveCurrentText()

}