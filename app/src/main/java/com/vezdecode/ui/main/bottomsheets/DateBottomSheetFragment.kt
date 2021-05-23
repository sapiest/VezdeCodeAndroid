package com.vezdecode.ui.main.bottomsheets

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.vezdecode.R
import com.vezdecode.base.BaseBottomSheetDialogFragment
import com.vezdecode.databinding.DataBottomLayoutBinding
import com.vezdecode.utils.DateUtils.getStringDate

class DateBottomSheetFragment(private val listener: DateBottomSheetListener) :
    BaseBottomSheetDialogFragment() {
    private var _binding: DataBottomLayoutBinding? = null
    private val binding get() = _binding!!

    private val datePickerHelper = DatePickerHelper()

    private fun createDatePickerDialog(textView: TextView) {
        val datePicker = DatePickerDialog(
            requireContext(),
            dateToCallback(textView),
            datePickerHelper.fromYear,
            datePickerHelper.fromMonth,
            datePickerHelper.fromDay
        )
        datePicker.show()
        datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.design_default_color_primary_dark
            )
        )
        datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.design_default_color_primary_dark
            )
        )
        datePicker.getButton(DatePickerDialog.BUTTON_NEUTRAL).setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.design_default_color_primary_dark
            )
        )
    }

    private var dateToCallback: (TextView) -> DatePickerDialog.OnDateSetListener = {
        DatePickerDialog.OnDateSetListener { view, year, month, day ->
            Log.e("date", day.toString())
            it.text = getStringDate(year, month, day)
        }
    }

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
            if (binding.startDate.text != getString(R.string.choose_date) && binding.endDate.text != getString(R.string.choose_date)
            ) {
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

        arguments?.let {
            val startDate = it.getString(START_DATE)
            startDate?.let {
                binding.startDate.text = it
            }

            val endDate = it.getString(END_DATE)
            endDate?.let {
                binding.endDate.text = it
            }
        }

        binding.startDate.setOnClickListener {
            createDatePickerDialog(it as TextView)
        }

        binding.endDate.setOnClickListener {
            createDatePickerDialog(it as TextView)
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

    companion object {
        const val START_DATE = "start_date"
        const val END_DATE = "end_date"

        fun newInstance(startDate: String?, endDate: String?, listener: DateBottomSheetListener) =
            DateBottomSheetFragment(listener).apply {
                arguments = Bundle().apply {
                    putString(START_DATE, startDate)
                    putString(END_DATE, endDate)
                }
            }
    }
}