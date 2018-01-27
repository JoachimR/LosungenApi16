package de.reiss.android.losungen.widget.weekly

import android.content.Context
import android.support.annotation.WorkerThread
import de.reiss.android.losungen.App
import de.reiss.android.losungen.widget.WidgetRemoteViewsFactory

class WeeklyWidgetRemoteViewsFactory(context: Context) : WidgetRemoteViewsFactory(context) {

    private val weeklyWidgetTextRefresher: WeeklyWidgetTextRefresher by lazy {
        App.component.weeklyWidgetTextRefresher
    }

    @WorkerThread
    override fun loadWidgetText(): String = weeklyWidgetTextRefresher.retrieveCurrentText()

}