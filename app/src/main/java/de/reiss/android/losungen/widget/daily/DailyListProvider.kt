package de.reiss.android.losungen.widget.daily

import android.content.Context
import de.reiss.android.losungen.widget.ListProvider

class DailyListProvider(context: Context) : ListProvider(context) {

    override fun widgetText(): String = DailyWidgetTextRefresher.currentWidgetText

}