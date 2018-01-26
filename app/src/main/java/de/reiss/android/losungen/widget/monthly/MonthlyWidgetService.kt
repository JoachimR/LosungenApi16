package de.reiss.android.losungen.widget.monthly

import android.content.Intent
import android.widget.RemoteViewsService

class MonthlyWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsService.RemoteViewsFactory =
            MonthlyWidgetRemoteViewsFactory(this.applicationContext)

}