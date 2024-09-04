package `in`.koreatech.koin.domain.util

import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateFormatUtil {

    fun getDayOfWeek(date: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.KOREAN)
                ?.toString() ?: ""
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