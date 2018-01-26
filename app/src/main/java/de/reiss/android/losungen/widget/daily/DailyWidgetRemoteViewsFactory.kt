package de.reiss.android.losungen.widget.daily

import android.content.Context
import de.reiss.android.losungen.App
import de.reiss.android.losungen.widget.WidgetRemoteViewsFactory

class DailyWidgetRemoteViewsFactory(context: Context) : WidgetRemoteViewsFactory(context) {

    private val dailyWidgetTextRefresher: DailyWidgetTextRefresher by lazy {
        App.component.dailyWidgetTextRefresher
    }

    override fun loadWidgetText(): String = dailyWidgetTextRefresher.retrieveCurrentText()

}