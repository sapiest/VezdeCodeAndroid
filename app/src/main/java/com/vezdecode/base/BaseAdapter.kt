package com.vezdecode.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vezdecode.BR

open class BaseAdapter<T>(
    @LayoutRes
    private val layoutId: Int,
    private val onItemClicked: (T) -> Unit,
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, BaseAdapter<T>.CustomViewHolder<T>>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder<T> {
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(parent.context),
                layoutId,
                parent,
                false
            )

        return CustomViewHolder<T>(binding)
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

    override fun onBindViewHolder(holder: CustomViewHolder<T>, position: Int) {
        val item = getItem(position)
        holder.bindTo(item)
    }

    inner class CustomViewHolder<V> constructor(
        private val binding: ViewDataBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindTo(
            item: V
        ) {
            // Bind item to layout to dispatch data to layout
            binding.setVariable(BR.item, item)
            binding.executePendingBindings()
        }
    }
}

