package de.reiss.android.losungen.widget.yearly

import android.content.Context
import de.reiss.android.losungen.widget.ListProvider

class YearlyListProvider(context: Context) : ListProvider(context) {

    override fun widgetText(): String = YearlyWidgetTextRefresher.currentWidgetText

}