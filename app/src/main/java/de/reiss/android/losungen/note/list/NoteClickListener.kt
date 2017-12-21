package de.reiss.android.losungen.note.list

import de.reiss.android.losungen.model.Note

interface NoteClickListener {

    fun onNoteClicked(note: Note)

}