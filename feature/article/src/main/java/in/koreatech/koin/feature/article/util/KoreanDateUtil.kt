package `in`.koreatech.koin.feature.article.util

import java.time.DayOfWeek
import java.time.LocalDate

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
