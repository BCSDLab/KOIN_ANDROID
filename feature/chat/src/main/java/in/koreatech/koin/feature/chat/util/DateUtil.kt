package `in`.koreatech.koin.feature.chat.util

import java.time.LocalDate
import timber.log.Timber

private val DATE_REGEX = "(\\d+)년 (\\d+)월 (\\d+)일".toRegex()

fun parseDateString(dateString: String): LocalDate {
    val matchResult = DATE_REGEX.find(dateString) ?: run {
        Timber.w("Failed to parse date string: %s", dateString)
        return LocalDate.now()
    }
    val (year, month, day) = matchResult.destructured
    return runCatching {
        LocalDate.of(year.toInt(), month.toInt(), day.toInt())
    }.getOrElse { exception ->
        Timber.w(exception, "Failed to create LocalDate from: %s", dateString)
        LocalDate.now()
    }
}
