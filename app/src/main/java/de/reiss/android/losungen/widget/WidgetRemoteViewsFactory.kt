package de.reiss.android.losungen.widget

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.annotation.WorkerThread
import de.reiss.android.losungen.App
import de.reiss.android.losungen.R
import de.reiss.android.losungen.logger.logError
import de.reiss.android.losungen.logger.logWarn
import de.reiss.android.losungen.preferences.AppPreferences
import de.reiss.android.losungen.util.htmlize
import java.util.*

abstract class WidgetRemoteViewsFactory(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private val list = ArrayList<CharSequence>()

    private val appPreferences: AppPreferences by lazy {
        App.component.appPreferences
    }

    @WorkerThread
    abstract fun loadWidgetText(): String

    override fun onCreate() {
    }

    override fun onDestroy() {
    }

    /**
     * From the documentation:
     * expensive tasks can be safely performed synchronously within this method.
     * In the interim, the old data will be displayed within the widget.
     */
    override fun onDataSetChanged() {
        list.clear()
        list.add(htmlize(loadWidgetText()))
    }

    override fun getCount() = list.size

    override fun getItemId(position: Int) = position.toLong()

    override fun hasStableIds() = false

    override fun getLoadingView() = null

    override fun getViewTypeCount() = 1

    override fun getViewAt(position: Int): RemoteViews {
        val remoteViewRow = RemoteViews(context.packageName, R.layout.widget_layout_list_row)
        try {
            applyUi(remoteViewRow)
        } catch (e: Exception) {
            logError(e) { "Error when trying to set widget" }
        }
        return remoteViewRow
    }

    private fun applyUi(remoteViewRow: RemoteViews) {
        val list = list
        val item = if (list.isNotEmpty()) list.first() else {
            logWarn { "widget list is empty when trying to apply" }
            return
        }

        remoteViewRow.apply {

            val widgetCentered = appPreferences.widgetCentered()

            val textView = if (widgetCentered)
                R.id.tv_widget_content_centered
            else
                R.id.tv_widget_content_uncentered

            setViewVisibility(
                R.id.tv_widget_content_centered,
                if (widgetCentered) View.VISIBLE else View.GONE
            )
            setViewVisibility(
                R.id.tv_widget_content_uncentered,
                if (widgetCentered) View.GONE else View.VISIBLE
            )

            setTextViewText(textView, item)

            setTextColor(textView, appPreferences.widgetFontColor())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setTextViewTextSize(
                    textView, TypedValue.COMPLEX_UNIT_SP,
                    appPreferences.widgetFontSize()
                )
            } else {
                setFloat(textView, "setTextSize", appPreferences.widgetFontSize())
            }

            // widget click event
            setOnClickFillInIntent(R.id.widget_row_root, Intent())

            // background color set in WidgetProvider
        }
    }

}