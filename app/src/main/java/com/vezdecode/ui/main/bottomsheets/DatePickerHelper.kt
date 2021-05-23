package com.vezdecode.ui.main.bottomsheets

import com.vezdecode.utils.DateUtils.getCurrentDay
import com.vezdecode.utils.DateUtils.getCurrentMonth
import com.vezdecode.utils.DateUtils.getCurrentYear

class DatePickerHelper {
    var fromYear = getCurrentYear()
    var fromMonth = getCurrentMonth()
    var fromDay = getCurrentDay()
}