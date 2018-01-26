package de.reiss.android.losungen.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import de.reiss.android.losungen.App
import de.reiss.android.losungen.R
import de.reiss.android.losungen.SplashScreenActivity
import de.reiss.android.losungen.logger.logErrorWithCrashlytics
import de.reiss.android.losungen.preferences.AppPreferences

abstract class WidgetProvider : AppWidgetProvider() {

    companion object {

        private const val REQUEST_CODE_CLICK_WIDGET = 13

    }

    protected val appPreferences: AppPreferences by lazy {
        App.component.appPreferences
    }

    abstract fun serviceIntent(context: Context, appWidgetId: Int): Intent

    override fun onUpdate(context: Context,
                          appWidgetManager: AppWidgetManager,
                          appWidgetIds: IntArray) {
        try {
            for (id in appWidgetIds) {
                appWidgetManager.updateAppWidget(id, createRemoteViews(context, id))
            }
        } catch (e: Exception) {
            logErrorWithCrashlytics(e) { "Error when updating widget" }
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    private fun createRemoteViews(context: Context, appWidgetId: Int): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.widget_layout)

        //RemoteViews Service needed to provide adapter for ListView
        val serviceIntent = serviceIntent(context, appWidgetId)
        serviceIntent.data = Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME))

        //setting adapter to listView of the widget
        remoteViews.setRemoteAdapter(R.id.widget_list_view, serviceIntent)

        setClickMethod(context, remoteViews)
        setBackground(context, remoteViews)
        return remoteViews
    }

    private fun setClickMethod(context: Context, remoteViews: RemoteViews) {
        val pendingIntent = PendingIntent.getActivity(context, REQUEST_CODE_CLICK_WIDGET,
                SplashScreenActivity.createIntent(context), PendingIntent.FLAG_UPDATE_CURRENT)
        remoteViews.setPendingIntentTemplate(R.id.widget_list_view, pendingIntent)
    }

    private fun setBackground(context: Context, remoteViews: RemoteViews) {
        val identifier = context.resources.getIdentifier(appPreferences.widgetBackground(),
                "drawable", context.packageName)
        remoteViews.setImageViewResource(R.id.widget_background, identifier)
    }

}