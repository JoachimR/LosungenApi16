package de.reiss.android.losungen.language.list

import android.view.View
import android.widget.TextView
import de.reiss.android.losungen.R
import de.reiss.android.losungen.language.LanguageClickListener
import de.reiss.android.losungen.util.extensions.onClick
import de.reiss.android.losungen.util.view.ListItemViewHolder
import de.reiss.android.losungen.util.view.StableListItem

class LanguageListItemViewHolder(
    layout: View,
    private val languageClickListener: LanguageClickListener
) : ListItemViewHolder(layout) {

    private val context = layout.context
    private val language = layout.findViewById<TextView>(R.id.language_item_language_code)
    private val languageName = layout.findViewById<TextView>(R.id.language_item_name)

    private var item: LanguageListItem? = null

    init {
        layout.onClick {
            item?.let {
                languageClickListener.onLanguageClicked(it.language)
            }
        }
    }

    override fun bindViews(item: StableListItem, isLastItem: Boolean) {
        if (item is LanguageListItem) {
            this.item = item
            language.text = context.getString(
                R.string.language_item_language_code,
                item.language.languageCode
            )
            languageName.text = item.language.name
        }
    }

}