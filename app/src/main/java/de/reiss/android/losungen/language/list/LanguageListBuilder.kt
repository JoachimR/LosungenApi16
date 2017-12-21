package de.reiss.android.losungen.language.list

import de.reiss.android.losungen.model.Language
import de.reiss.android.losungen.util.view.StableListItem

object LanguageListBuilder {

    fun buildList(languages: List<Language>): List<StableListItem> = languages
            .sortedBy { it.key }
            .map { LanguageListItem(it) }

}
