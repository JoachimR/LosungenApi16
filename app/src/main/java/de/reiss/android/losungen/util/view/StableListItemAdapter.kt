package de.reiss.android.losungen.util.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil


abstract class StableListItemAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<ListItemViewHolder>() {

    init {
        setHasStableIds(true)
    }

    final override fun setHasStableIds(hasStableIds: Boolean) {
        if (!hasStableIds) {
            throw IllegalArgumentException("StableListItemAdapter does not allow unstable ids")
        }
        super.setHasStableIds(hasStableIds)
    }

    protected var list = emptyList<StableListItem>()

    fun updateContent(content: List<StableListItem>) {
        val newList = content.toList()
        val diffResult = DiffUtil.calculateDiff(StableDiffCallback(list, newList))
        list = newList
        diffResult.dispatchUpdatesTo(this)
    }

    protected fun inflate(viewGroup: ViewGroup, @LayoutRes layoutResId: Int): View =
            LayoutInflater.from(viewGroup.context).inflate(layoutResId, viewGroup, false)

    override fun getItemId(position: Int): Long = getItem(position).stableId()

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.bindViews(item = getItem(position), isLastItem = position == itemCount - 1)
    }

    override fun getItemCount(): Int = list.size

    protected fun getItem(position: Int): StableListItem = list[position]

    private class StableDiffCallback(private val oldList: List<StableListItem>,
                                     private val newList: List<StableListItem>)
        : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                oldList[oldItemPosition].stableId() == newList[newItemPosition].stableId()

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                oldList[oldItemPosition] == newList[newItemPosition]

    }

}