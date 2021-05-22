package com.vezdecode.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vezdecode.data.remote.model.IncidentModel
import com.vezdecode.BR

class IncidentsAdapter(
    @LayoutRes private val layoutId: Int,
    private val onItemClicked: (IncidentModel) -> Unit
) : ListAdapter<IncidentModel, IncidentsAdapter.CustomViewHolder<IncidentModel>>(
    IncidentDiffCallback()
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomViewHolder<IncidentModel> {
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(parent.context),
                layoutId,
                parent,
                false
            )

        return CustomViewHolder<IncidentModel>(binding)
            .apply {
                onViewHolderCreated(this, binding)
            }
    }

    private fun onViewHolderCreated(
        viewHolder: RecyclerView.ViewHolder,
        binding: ViewDataBinding
    ) {

        binding.root.setOnClickListener {
            onItemClicked(getItem(viewHolder.adapterPosition))
        }

    }

    override fun onBindViewHolder(holder: CustomViewHolder<IncidentModel>, position: Int) {
        val item = getItem(position)
        holder.bindTo(item)
    }

    inner class CustomViewHolder<T> constructor(
        private val binding: ViewDataBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindTo(
            item: T
        ) {
            // Bind item to layout to dispatch data to layout
            binding.setVariable(BR.item, item)
            binding.executePendingBindings()
        }

    }

}

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