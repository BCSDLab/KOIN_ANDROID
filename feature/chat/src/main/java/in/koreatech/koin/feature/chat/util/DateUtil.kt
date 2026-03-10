package `in`.koreatech.koin.feature.chat.util

import java.time.LocalDate
import timber.log.Timber

private val DATE_REGEX = Regex("""^(\d+)년 (\d+)월 (\d+)일$""")

fun parseDateString(dateString: String): LocalDate? {
    val matchResult = DATE_REGEX.matchEntire(dateString) ?: run {
        Timber.w("Failed to parse date string: %s", dateString)
        return null
    }
    val (year, month, day) = matchResult.destructured
    return runCatching {
        LocalDate.of(year.toInt(), month.toInt(), day.toInt())
    }.getOrElse { exception ->
        Timber.w(exception, "Failed to create LocalDate from: %s", dateString)
        null
    }
}
