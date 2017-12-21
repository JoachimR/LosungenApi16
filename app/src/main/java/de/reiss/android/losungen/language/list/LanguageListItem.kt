package de.reiss.android.losungen.language.list

import de.reiss.android.losungen.model.Language
import de.reiss.android.losungen.util.view.StableListItem

data class LanguageListItem(val language: Language) : StableListItem() {

    override fun stableId() = hashCode().toLong()

}
