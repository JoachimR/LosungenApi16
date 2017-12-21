package de.reiss.android.losungen.note.list

import de.reiss.android.losungen.model.Note
import de.reiss.android.losungen.util.view.StableListItem

data class NoteListItem(val note: Note) : StableListItem() {

    override fun stableId() = hashCode().toLong()

}
