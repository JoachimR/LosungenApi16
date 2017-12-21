package de.reiss.android.losungen.note.list

import de.reiss.android.losungen.model.Note
import de.reiss.android.losungen.util.view.StableListItem

object NoteListBuilder {

    fun buildList(notes: List<Note>): List<StableListItem> = notes
            .sortedBy { it.date }
            .map { NoteListItem(it) }

}