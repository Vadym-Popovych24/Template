package com.android.template.utils.helpers

import android.text.format.DateUtils
import com.android.template.R
import com.android.template.utils.getPluralStringFromResource
import com.android.template.utils.getStringFromResource
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

val gmtDateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'", Locale.getDefault())
val newsServerDateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'", Locale.getDefault())
val newsUIDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault())
val notificationsDateFormat =
    SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSSSSSZZZZZ", Locale.getDefault())
val experiencesDateFormat = SimpleDateFormat("MM/dd/yyyy h:mm:ss a", Locale.getDefault())
val messagesDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX", Locale.getDefault())
val contactRequestsDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS", Locale.getDefault())
const val messagesFormatLength = "2021-04-21T08:59:51".length
val onlyDayOfWeekFormat = SimpleDateFormat("EEEE", Locale.getDefault())
val hoursMinutesFormat = SimpleDateFormat("HH:mm", Locale.UK)
val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.UK)
val defaultDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.UK)
val pickerDateFormat = SimpleDateFormat("EEE MMM d hh:mm:ss z yyyy", Locale.UK)
val monthDateFormat = SimpleDateFormat("MMM yyyy", Locale.getDefault())
val messageHoursMinutesFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
val messageYearMonthDayFormat = SimpleDateFormat("yyyy.dd.MM:HH:mm", Locale.getDefault())
val movieDataFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR
const val WEEK = DAY * 7
const val MONTH = DAY * 30

fun Date.toServerFormatString(): String = gmtDateFormat.format(this)

fun convert(source: String, from: DateFormat, to: DateFormat): String {
    val date = try {
        from.parse(source)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    return if (date != null) {
        to.format(date)
    } else {
        source
    }
}

fun convertToDate(stringDate: String?): Date? {
    if (stringDate == null) return null
    return try {
        simpleDateFormat.parse(stringDate)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun getDateFromMilliseconds(milliSeconds: Long, dateFormat: SimpleDateFormat) : String
{
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = milliSeconds
    return dateFormat.format(calendar.time)
}

fun relativeFormat(date: Date): String {
    val units = arrayOf(MONTH, WEEK, DAY, HOUR, MINUTE, SECOND)

    val unitLabels = arrayOf(
        R.plurals.months,
        R.plurals.weeks,
        R.plurals.days,
        R.plurals.hours,
        R.plurals.minutes,
        R.plurals.seconds
    )

    var result = ""
    val difference = System.currentTimeMillis() - date.time

    if (difference < MINUTE / 2){
        result = R.string.just_now.getStringFromResource
    }
    else {

        units.forEachIndexed { index, unit ->
            val value = (difference / unit).toInt()
            result =
                "$value ${unitLabels[index].getPluralStringFromResource(value)} ${R.string.ago.getStringFromResource}"
            if (value > 0)
                return result
        }
    }
    return result
}

fun messageRelativeFormat(time: Long): String {
    val isToday = DateUtils.isToday(time)
    return if (isToday) {
        messageHoursMinutesFormat.format(time)
    } else {
       convert(getDateFromMilliseconds(time, messagesDateFormat), messagesDateFormat, messageYearMonthDayFormat).format(time)
    }
}

fun getCurrentDataInMessageFormat(): String = messagesDateFormat.format(Date())

