package com.vezdecode.utils

import android.annotation.SuppressLint
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

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

    @SuppressLint("SimpleDateFormat")
    fun stringDateToLong(value: String?): Long {
        val newFormat = SimpleDateFormat("dd MMM yyyy")
        val curDate = if(value != null) {
            newFormat.parse(value)
        }else null
        return curDate?.time ?: 0L
    }

    @SuppressLint("SimpleDateFormat")
    fun stringDateFromLong(value: Long?): String?{
        if(value == null) return null
        val date = Date(value)
        val newFormat = SimpleDateFormat("dd MMM yyyy")
        return newFormat.format(date)
    }

    fun getCurrentDate(): Long {
        val calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
        return calendar.time.time
    }

    fun getCurrentYear(): Int {
        val calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
        calendar.time = Date()
        return calendar.get(Calendar.YEAR)
    }

    fun getCurrentMonth(): Int {
        val calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
        calendar.time = Date()
        return calendar.get(Calendar.MONTH)
    }

    fun getCurrentDay(): Int {
        val calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
        calendar.time = Date()
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    fun getStringDate(year: Int, month: Int, day: Int): String {
        when (month - 1) {
            0 -> return "$day января $year"
            1 -> return "$day февраля $year"
            2 -> return "$day марта $year"
            3 -> return "$day апреля $year"
            4 -> return "$day мая $year"
            5 -> return "$day июня $year"
            6 -> return "$day июля $year"
            7 -> return "$day августа $year"
            8 -> return "$day сентября $year"
            9 -> return "$day октября $year"
            10 -> return "$day ноября $year"
            11 -> return "$day декабря $year"
        }
        return ""
    }
}