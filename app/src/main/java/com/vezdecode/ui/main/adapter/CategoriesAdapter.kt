package com.vezdecode.ui.main.adapter

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import com.vezdecode.base.BaseAdapter
import com.vezdecode.data.remote.model.IncidentModel

class CategoriesAdapter(
    @LayoutRes layoutId: Int,
    onItemClicked: (String) -> Unit
) : BaseAdapter<String>(layoutId, onItemClicked, CategoriesDiffCallback())

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minimum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class CategoriesDiffCallback : DiffUtil.ItemCallback<String>() {

    override fun areItemsTheSame(
        oldItem: String, newItem: String
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: String, newItem: String
    ): Boolean {
        return oldItem == newItem
    }
}