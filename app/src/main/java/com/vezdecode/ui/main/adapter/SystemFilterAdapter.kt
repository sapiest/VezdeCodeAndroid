package com.vezdecode.ui.main.adapter

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import com.vezdecode.base.BaseAdapter
import com.vezdecode.data.remote.model.SystemModel

class SystemFilterAdapter(
    @LayoutRes layoutId: Int,
    onItemClicked: (SystemModel) -> Unit
) : BaseAdapter<SystemModel>(layoutId, onItemClicked, SystemsDiffCallback())

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minimum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class SystemsDiffCallback : DiffUtil.ItemCallback<SystemModel>() {

    override fun areItemsTheSame(
        oldItem: SystemModel, newItem: SystemModel
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: SystemModel, newItem: SystemModel
    ): Boolean {
        return oldItem == newItem
    }
}