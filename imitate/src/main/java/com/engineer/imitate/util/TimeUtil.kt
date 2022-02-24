package com.engineer.imitate.util

import java.text.SimpleDateFormat
import java.util.*

object TimeUtilTool {

    private const val BASE = 1000

    private const val ONE_MINUTE = 60f
    private const val ONE_HOUR = ONE_MINUTE * ONE_MINUTE
    private const val ONE_DAY = ONE_HOUR * 24
    private const val ONE_YEAR = ONE_DAY * 365

    fun millionSecondsToMinutes(timeInMillionSeconds: Long): Float {
        val second = timeInMillionSeconds / BASE
        return second / ONE_MINUTE
    }

    fun millionSecondsToHour(timeInMillionSeconds: Long): Float {
        val second = timeInMillionSeconds / BASE
        return second / ONE_HOUR
    }

    fun millionSecondsToDay(timeInMillionSeconds: Long): Float {
        val second = timeInMillionSeconds / BASE
        return second / ONE_DAY
    }

    fun millionSecondsToYear(timeInMillionSeconds: Long): Float {
        val second = timeInMillionSeconds / BASE
        return second / ONE_YEAR
    }

    fun timeStampToDate(timeInMillionSeconds: Long): String {
        val format = "yyyy-MM-dd HH:mm:ss"
        val sdf = SimpleDateFormat(format)
        return sdf.format(Date(timeInMillionSeconds))
    }

}