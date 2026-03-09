package `in`.koreatech.koin.feature.chat.util

import java.time.LocalDate

fun parseDateString(dateString: String): LocalDate {
    val regex = "(\\d+)년 (\\d+)월 (\\d+)일".toRegex()
    val matchResult = regex.find(dateString) ?: return LocalDate.now()
    val (year, month, day) = matchResult.destructured
    return runCatching {
        LocalDate.of(year.toInt(), month.toInt(), day.toInt())
    }.getOrDefault(LocalDate.now())
}
