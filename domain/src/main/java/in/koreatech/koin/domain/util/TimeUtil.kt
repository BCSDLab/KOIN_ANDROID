package `in`.koreatech.koin.domain.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object TimeUtil {
    private val simpleDateFormat = SimpleDateFormat()
    fun getCurrentTime(): Date {
        return Calendar.getInstance().time
    }

    fun isWeekend(dateString: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(dateString, formatter)
        return date.dayOfWeek.value == 6 || date.dayOfWeek.value == 7
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
        return try {
            simpleDateFormat.parse(date)
        } catch (e: ParseException) {
            stringToDateYYMMDD(date)
        }
    }

    fun stringToDateYYMMDD(date: String): Date {
        simpleDateFormat.applyPattern("yyMMdd")
        return simpleDateFormat.parse(date)
    }

    fun compareWithCurrentTime(time: String): Long { //HH:mm
        val compareTimeString = dateFormatToYYYYMMDD(getCurrentTime()) + " " + time
        simpleDateFormat.applyPattern("yyyy-MM-dd HH:mm")
        return simpleDateFormat.parse(compareTimeString).time - getCurrentTime().time
    }

    fun formatDateToKorean(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd")
        val outputFormat = SimpleDateFormat("M월 d일")
        val date: Date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }

    fun getDateDifferenceWithToday(date: Date): Int {
        return ((date.time - getCurrentTime().time) / (60 * 60 * 24 * 1000)).toInt()
    }

    fun getDateDifferenceInDays(date1: Date, date2: Date): Int {
        val calendar1 = Calendar.getInstance().apply {
            time = date1
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val calendar2 = Calendar.getInstance().apply {
            time = date2
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val diffInMillis = calendar1.timeInMillis - calendar2.timeInMillis
        return (diffInMillis / (1000 * 60 * 60 * 24)).toInt()
    }

    fun isToday(dateString: String): Boolean {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd")
        val date: Date = inputFormat.parse(dateString)
        val today = Calendar.getInstance().time
        val todayString = inputFormat.format(today)
        return dateString == todayString
    }

    fun isTomorrow(dateString: String): Boolean {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd")
        val date: Date = inputFormat.parse(dateString)
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 1)
        val tomorrow = calendar.time
        val tomorrowString = inputFormat.format(tomorrow)
        return dateString == tomorrowString
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