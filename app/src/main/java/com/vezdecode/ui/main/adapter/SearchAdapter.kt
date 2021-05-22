package com.vezdecode.ui.main.adapter

import android.util.Log
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import com.vezdecode.base.BaseAdapter
import com.vezdecode.data.remote.model.DescriptionModel


class SearchAdapter(
    @LayoutRes layoutId: Int,
    onItemClicked: (DescriptionModel) -> Unit
) : BaseAdapter<DescriptionModel>(
    layoutId, onItemClicked, SearchDiffCallback()
), Filterable {

    private var queryText: String? = null
    private var fullData: List<DescriptionModel>? = null

    override fun submitList(list: MutableList<DescriptionModel>?) {
        super.submitList(list)

        if(fullData == null) {
            fullData = list
        }
    }

    override fun getFilter(): Filter {
        return mFilter
    }

    val mFilter = object : Filter() {
        override fun performFiltering(sequence: CharSequence?): FilterResults? {
            if (sequence.isNullOrEmpty()) return null

            queryText = sequence.toString()

            val newList = fullData?.filter {
                it.description.lowercase().contains(sequence.toString().lowercase())
            }

            val filterResults = FilterResults()
            filterResults.values = newList
            filterResults.count = newList?.size ?: 0

            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            Log.e("res", results?.count.toString())

            if(results == null){
                submitList(ArrayList(fullData))
            }else{
                val data = results.values as ArrayList<DescriptionModel>
                if(queryText != null) {
                    val newData = data.map {
                        val startInd = it.description.lowercase().indexOf(queryText!!)
                        val endInt = startInd + queryText!!.length
                        if(startInd != -1){
                            val oldValue = it.description.substring(startInd, endInt)
                            val desc= it.description.replaceRange(startInd, endInt, "<span style=\"background-color: #FFFF00\">$oldValue</span>")
                            it.copy(description = desc)
                        }else{
                            it
                        }
                    }
                    submitList(ArrayList(newData))
                }else {
                    submitList(data)
                }
            }
        }
    }

//    private fun selectText(elem: DescriptionModel, searchText: String): DescriptionModel{
//        var desc = elem.description
//        var index: Int = desc.lowercase().indexOf(searchText)
//        while (index > 0) {
////            val sb = SpannableStringBuilder(desc)
////            val fcs = ForegroundColorSpan(Color.rgb(158, 158, 158)) //specify color here
////            sb.setSpan(fcs, index, searchText.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
//            desc= desc.replace(searchText, "<font color='#c5c5c5'>${desc[index]}</font>")
//            index = desc.lowercase().indexOf(searchText, index + 1)
//        }
//        return elem.copy(description = desc)
//    }
}

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minimum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class SearchDiffCallback : DiffUtil.ItemCallback<DescriptionModel>() {

    override fun areItemsTheSame(
        oldItem: DescriptionModel, newItem: DescriptionModel
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: DescriptionModel, newItem: DescriptionModel
    ): Boolean {
        return oldItem.id == newItem.id
    }
}