package com.vezdecode.utils

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visibilityBasedOn")
fun View.visibilityBasedOn(condition: Boolean) {
    visibility = if (condition) View.VISIBLE else View.GONE
}