package `in`.koreatech.koin.feature.recruitment.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

fun LocalDate.toDateText(): String = this.format(DATE_FORMATTER)
