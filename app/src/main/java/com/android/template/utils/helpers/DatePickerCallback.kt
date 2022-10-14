package com.android.template.utils.helpers

import android.app.DatePickerDialog
import android.widget.DatePicker
import java.util.*

class DatePickerCallback(private val callback: (Date) -> Unit) :
    DatePickerDialog.OnDateSetListener {

    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth, 0, 0)
        calendar.timeZone = TimeZone.getTimeZone("UTC")
        callback(calendar.time)
    }
}