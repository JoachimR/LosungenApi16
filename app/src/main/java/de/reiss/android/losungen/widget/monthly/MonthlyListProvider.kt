package de.reiss.android.losungen.widget.monthly

import android.content.Context
import de.reiss.android.losungen.widget.ListProvider

class MonthlyListProvider(context: Context) : ListProvider(context) {

    override fun widgetText(): String = MonthlyWidgetTextRefresher.currentWidgetText

}