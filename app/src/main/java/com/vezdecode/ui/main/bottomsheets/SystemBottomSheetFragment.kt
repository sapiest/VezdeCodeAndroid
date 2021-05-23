package com.vezdecode.ui.main.bottomsheets

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.vezdecode.R
import com.vezdecode.base.BaseBottomSheetDialogFragment
import com.vezdecode.data.remote.model.SystemModel
import com.vezdecode.databinding.SystemBottomLayoutBinding
import com.vezdecode.ui.main.adapter.SystemFilterAdapter


class SystemBottomSheetFragment(private val listener: SystemBottomSheetListener) :
    BaseBottomSheetDialogFragment() {
    private var _binding: SystemBottomLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SystemBottomLayoutBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val list = it.getParcelableArrayList<SystemModel>(LIST_KEY)
            binding.rvFilters.apply {
                this.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

                this.adapter = SystemFilterAdapter(R.layout.filter_item_list) { item ->
                    (binding.rvFilters.adapter as? ListAdapter<SystemModel, *>)?.currentList?.let { list ->
                        val index = list.indexOf(item)
                        val newList = ArrayList(list)
                        newList[index] = item.copy(isActive = !item.isActive)
                        (this.adapter as ListAdapter<SystemModel, *>).submitList(newList)
                    }
                }
                this.itemAnimator = null
            }

            binding.btnApply.setOnClickListener {
                (binding.rvFilters.adapter as? ListAdapter<SystemModel, *>)?.currentList?.let {
                    listener.onSystemApplyButton(it.filter { it.isActive })
                }
                dismiss()
            }

            binding.btnCancel.setOnClickListener {
                listener.onSystemDiscardButton()
                dismiss()
            }

            (binding.rvFilters.adapter as ListAdapter<SystemModel, *>).submitList(list)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val LIST_KEY = "list_systems"

        fun newInstance(list: List<SystemModel>, listener: SystemBottomSheetListener) =
            SystemBottomSheetFragment(listener).apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(LIST_KEY, ArrayList(list))
                }
            }
    }

    interface SystemBottomSheetListener {
        fun onSystemApplyButton(selectedList: List<SystemModel>)
        fun onSystemDiscardButton()
    }
}