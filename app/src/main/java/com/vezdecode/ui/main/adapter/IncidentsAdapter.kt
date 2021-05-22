package com.vezdecode.ui.main.adapter

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import com.vezdecode.base.BaseAdapter
import com.vezdecode.data.remote.model.IncidentModel

class IncidentsAdapter(
    @LayoutRes layoutId: Int,
    onItemClicked: (IncidentModel) -> Unit
) : BaseAdapter<IncidentModel>(layoutId, onItemClicked, IncidentDiffCallback())

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minimum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class IncidentDiffCallback : DiffUtil.ItemCallback<IncidentModel>() {

    override fun areItemsTheSame(
        oldItem: IncidentModel, newItem: IncidentModel
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: IncidentModel, newItem: IncidentModel
    ): Boolean {
        return oldItem.ticketId == newItem.ticketId
    }
}