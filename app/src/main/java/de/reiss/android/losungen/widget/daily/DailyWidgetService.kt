package de.reiss.android.losungen.widget.daily

import android.content.Intent
import android.widget.RemoteViewsService

class DailyWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsService.RemoteViewsFactory =
            DailyWidgetRemoteViewsFactory(this.applicationContext)

}