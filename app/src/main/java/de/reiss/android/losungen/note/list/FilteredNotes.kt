package de.reiss.android.losungen.note.list

import de.reiss.android.losungen.model.Note

data class FilteredNotes constructor(
    val allItems: List<Note>,
    val filteredItems: List<Note>,
    val query: String
) {

    constructor() : this(emptyList(), emptyList(), "")
}