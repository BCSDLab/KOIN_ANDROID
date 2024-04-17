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
}