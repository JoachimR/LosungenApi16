package de.reiss.android.losungen.util.view

import android.view.View

abstract class ListItemViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

    abstract fun bindViews(item: StableListItem, isLastItem: Boolean)

}