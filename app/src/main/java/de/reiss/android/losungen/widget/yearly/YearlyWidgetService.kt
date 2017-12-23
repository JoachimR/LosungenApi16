package de.reiss.android.losungen.widget.yearly

import android.content.Intent
import android.widget.RemoteViewsService

class YearlyWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsService.RemoteViewsFactory =
            YearlyListProvider(this.applicationContext)

}