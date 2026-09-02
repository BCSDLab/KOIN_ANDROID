package `in`.koreatech.koin.feature.recruitment.utils

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

fun getDayOfWeek(dayOfWeek: DayOfWeek): String =
    when (dayOfWeek) {
        DayOfWeek.MONDAY -> "월"
        DayOfWeek.TUESDAY -> "화"
        DayOfWeek.WEDNESDAY -> "수"
        DayOfWeek.THURSDAY -> "목"
        DayOfWeek.FRIDAY -> "금"
        DayOfWeek.SATURDAY -> "토"
        DayOfWeek.SUNDAY -> "일"
    }

fun LocalDate.toDateWithDayOfWeekText(): String =
    "${this.format(DATE_FORMATTER)} (${getDayOfWeek(this.dayOfWeek)})"

fun LocalDate.toDateText(): String =
    this.format(DATE_FORMATTER)
