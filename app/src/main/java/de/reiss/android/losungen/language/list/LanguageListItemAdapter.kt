package de.reiss.android.losungen.language.list

import android.view.ViewGroup
import de.reiss.android.losungen.R
import de.reiss.android.losungen.language.LanguageClickListener
import de.reiss.android.losungen.util.view.StableListItemAdapter

class LanguageListItemAdapter(private val languageClickListener: LanguageClickListener) :
    StableListItemAdapter() {

    override fun getItemViewType(position: Int) =
        when (getItem(position)) {

            is LanguageListItem -> R.layout.language_item

            else -> -1
        }


    override fun onCreateViewHolder(group: ViewGroup, viewType: Int) =
        when (viewType) {

            R.layout.language_item -> {
                LanguageListItemViewHolder(inflate(group, viewType), languageClickListener)
            }

            else -> throw IllegalStateException("Invalid viewType " + viewType)
        }

}
