package de.reiss.android.losungen.widget

import android.content.Intent
import android.widget.RemoteViewsService

class WidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsService.RemoteViewsFactory =
            ListProvider(this.applicationContext)

}