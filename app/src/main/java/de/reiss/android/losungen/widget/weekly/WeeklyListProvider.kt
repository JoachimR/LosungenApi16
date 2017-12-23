package de.reiss.android.losungen.widget.weekly

import android.content.Context
import de.reiss.android.losungen.widget.ListProvider

class WeeklyListProvider(context: Context) : ListProvider(context) {

    override fun widgetText(): String = WeeklyWidgetTextRefresher.currentWidgetText

}