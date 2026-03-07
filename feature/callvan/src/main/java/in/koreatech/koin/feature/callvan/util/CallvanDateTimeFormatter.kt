package `in`.koreatech.koin.feature.callvan.util

import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

fun formatDateTime(date: String, time: String): String {
    val localDate = LocalDate.parse(date)
    val month = localDate.monthValue.toString().padStart(2, '0')
    val day = localDate.dayOfMonth.toString().padStart(2, '0')
    val dayOfWeek = localDate.dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.KOREAN)
    return "$month.$day($dayOfWeek) $time"
}
