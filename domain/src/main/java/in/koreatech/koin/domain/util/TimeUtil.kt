package `in`.koreatech.koin.domain.util

import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {
    private val simpleDateFormat = SimpleDateFormat()
    fun getCurrentTime(): Date {
        return Calendar.getInstance().time
    }

    fun getNextDayDate(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DATE, 1)
        return calendar.time
    }

    fun getPreviousDayDate(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DATE, -1)
        return calendar.time
    }

    fun dateFormatToYYYYMMDD(date: Date): String {
        simpleDateFormat.applyPattern("yyyy-MM-dd")
        return simpleDateFormat.format(date)
    }

    fun dateFormatToYYMMDD(date: Date): String {
        simpleDateFormat.applyPattern("yyMMdd")
        return simpleDateFormat.format(date)
    }

    fun dateFormatToMMDDEE(date: Date): String {
        simpleDateFormat.applyPattern("MM/dd (EE)")
        return simpleDateFormat.format(date)
    }

    fun stringToDateYYYYMMDD(date: String): Date {
        simpleDateFormat.applyPattern("yyyy-MM-dd")
        return simpleDateFormat.parse(date)
    }

    fun compareWithCurrentTime(time: String): Long { //HH:mm
        val compareTimeString = dateFormatToYYYYMMDD(getCurrentTime()) + " " + time
        simpleDateFormat.applyPattern("yyyy-MM-dd HH:mm")
        return simpleDateFormat.parse(compareTimeString).time - getCurrentTime().time
    }

    fun getDateDifferenceWithToday(date: Date): Int {
        return ((date.time - getCurrentTime().time) / (60 * 60 * 24 * 1000)).toInt()
    }

    fun isBetweenCurrentTime(starTime: String, endTime: String): Boolean {
        return if (starTime.isEmpty() || endTime.isEmpty()) {
            true
        } else {
            try {
                val parser = SimpleDateFormat("HH:mm")
                val curTime = parser.format(Date())
                val start = parser.parse(starTime)
                val end = parser.parse(endTime)
                val current = parser.parse(curTime)
                if (end.before(start)) {
                    val cal = Calendar.getInstance()
                    cal.time = end
                    cal.add(Calendar.DAY_OF_YEAR, 1)
                    end.time = cal.timeInMillis
                }
                current.after(start) && current.before(end)
            } catch (e: Exception) {
                false
            }
        }
    }
}