package de.reiss.android.losungen.widget.daily

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import de.reiss.android.losungen.App
import de.reiss.android.losungen.R
import de.reiss.android.losungen.widget.WidgetProvider

class DailyWidgetProvider : WidgetProvider() {

    companion object {

        fun refresh() {
            val context = App.component.context
            val appWidgetManager = AppWidgetManager.getInstance(context)

            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                ComponentName(context, DailyWidgetProvider::class.java)
            )

            context.sendBroadcast(
                Intent(context, DailyWidgetProvider::class.java)
                    .putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
                    .setAction("android.appwidget.action.APPWIDGET_UPDATE")
            )

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view)
        }

    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(
            ComponentName(context, DailyWidgetProvider::class.java)
        )
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view)
    }

    override fun serviceIntent(context: Context, appWidgetId: Int): Intent =
        Intent(context, DailyWidgetService::class.java)
            .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

}