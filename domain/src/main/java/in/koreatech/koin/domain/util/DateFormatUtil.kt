package `in`.koreatech.koin.domain.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateFormatUtil {

    /**
     * Date에 해당하는 요일
     */
    fun getDayOfWeek(date: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.KOREAN)
                ?.toString() ?: ""
    }

    /**
     * yyyy-MM-dd HH:mm:ss -> MM.dd
     */
    fun getSimpleMonthAndDay(dateString: String): String {
        val originalFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val targetFormat = SimpleDateFormat("MM.dd", Locale.getDefault())
        val date = originalFormat.parse(dateString)
        return targetFormat.format(date)
    }

    fun dayOfWeekToIndex(dayOfWeek: String): Int {
        return when (dayOfWeek) {
            "SUNDAY" -> 0
            "MONDAY" -> 1
            "TUESDAY" -> 2
            "WEDNESDAY" -> 3
            "THURSDAY" -> 4
            "FRIDAY" -> 5
            "SATURDAY" -> 6
            else -> -1
        }
    }
}