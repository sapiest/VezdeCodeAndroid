package com.vezdecode.utils

import android.annotation.SuppressLint
import android.util.Log
import java.text.SimpleDateFormat

object DateUtils {
    @SuppressLint("SimpleDateFormat")
    fun getDMYFormat(value: String): String {
        val curString = value.substring(0, value.length - 6)
        Log.e("dateSubsctring", curString)
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val newFormat = SimpleDateFormat("dd MMM yyyy")
        val curDate = format.parse(curString)
        return newFormat.format(curDate!!)
    }
}