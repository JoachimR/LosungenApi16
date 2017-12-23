package de.reiss.android.losungen.widget.weekly

import android.content.Intent
import android.widget.RemoteViewsService

class WeeklyWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsService.RemoteViewsFactory =
            WeeklyListProvider(this.applicationContext)

}