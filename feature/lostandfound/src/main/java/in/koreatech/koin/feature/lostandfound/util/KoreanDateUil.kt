package `in`.koreatech.koin.feature.lostandfound.util

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.getKoreanDayOfWeekName(): String {
    return when (dayOfWeek) {
        DayOfWeek.MONDAY -> "월요일"
        DayOfWeek.TUESDAY -> "화요일"
        DayOfWeek.WEDNESDAY -> "수요일"
        DayOfWeek.THURSDAY -> "목요일"
        DayOfWeek.FRIDAY -> "금요일"
        DayOfWeek.SATURDAY -> "토요일"
        DayOfWeek.SUNDAY -> "일요일"
        else -> throw IllegalArgumentException("Invalid day of week")
    }
}

fun LocalDate.getKoreanDayOfWeekShortName(): String {
    return this.getKoreanDayOfWeekName().substring(0, 1)
}

fun LocalDate.getKoreanMMddEForm(): String {
    val formatType = DateTimeFormatter.ofPattern("MM.dd")
    return "${this.format(formatType)} ${this.getKoreanDayOfWeekName().substring(0, 1)}"
}