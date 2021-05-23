package com.vezdecode.ui.main.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vezdecode.base.BaseBottomSheetDialogFragment
import com.vezdecode.databinding.DataBottomLayoutBinding

class DateBottomSheetFragment(private val listener: DateBottomSheetListener) :
    BaseBottomSheetDialogFragment() {
    private var _binding: DataBottomLayoutBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBottomLayoutBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnApply.setOnClickListener {
            if (binding.startDate.text.isNotEmpty() && binding.endDate.text.isNotEmpty()) {
                listener.onDateApplyButton(
                    binding.startDate.text.toString(),
                    binding.endDate.text.toString()
                )
            }
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            listener.onDateDiscardButton()
            dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    interface DateBottomSheetListener {
        fun onDateApplyButton(startDate: String, endDate: String)
        fun onDateDiscardButton()
    }
}