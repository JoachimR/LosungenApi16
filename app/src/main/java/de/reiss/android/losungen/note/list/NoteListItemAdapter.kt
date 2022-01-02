package de.reiss.android.losungen.note.list

import android.view.ViewGroup
import de.reiss.android.losungen.R
import de.reiss.android.losungen.util.view.StableListItemAdapter

class NoteListItemAdapter(private val noteClickListener: NoteClickListener) :
    StableListItemAdapter() {

    override fun getItemViewType(position: Int) =
        when (getItem(position)) {

            is NoteListItem -> R.layout.note_list_item

            else -> -1
        }

    override fun onCreateViewHolder(group: ViewGroup, viewType: Int) =
        when (viewType) {

            R.layout.note_list_item -> {
                NoteListItemViewHolder(inflate(group, viewType), noteClickListener)
            }

            else -> throw IllegalStateException("Invalid viewType " + viewType)
        }

}