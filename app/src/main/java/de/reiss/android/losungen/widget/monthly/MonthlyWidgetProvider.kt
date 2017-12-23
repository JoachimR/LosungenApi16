package de.reiss.android.losungen.widget.monthly

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import de.reiss.android.losungen.App
import de.reiss.android.losungen.R
import de.reiss.android.losungen.widget.WidgetProvider

class MonthlyWidgetProvider : WidgetProvider() {

    companion object {

        fun triggerWidgetRefresh() {
            val context = App.component.context
            val appWidgetManager = AppWidgetManager.getInstance(context)

            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                    ComponentName(context, MonthlyWidgetProvider::class.java))

            context.sendBroadcast(Intent(context, MonthlyWidgetProvider::class.java)
                    .putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
                    .setAction("android.appwidget.action.APPWIDGET_UPDATE"))

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view)
        }

    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        AppWidgetManager.getInstance(context).apply {
            notifyAppWidgetViewDataChanged(
                    getAppWidgetIds(ComponentName(context, MonthlyWidgetProvider::class.java)),
                    R.id.widget_list_view)
        }
    }

    override fun serviceIntent(context: Context, appWidgetId: Int): Intent =
            Intent(context, MonthlyWidgetService::class.java)
                    .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

}