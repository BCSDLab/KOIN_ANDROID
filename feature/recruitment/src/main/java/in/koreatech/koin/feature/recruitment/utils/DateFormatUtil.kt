package `in`.koreatech.koin.feature.recruitment.utils

import `in`.koreatech.koin.feature.recruitment.model.StableLocalDate
import `in`.koreatech.koin.feature.recruitment.model.toStable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
private val API_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

fun LocalDate.toDateText(): String = this.format(DATE_FORMATTER)

fun LocalDate.toApiDateText(): String = this.format(API_DATE_FORMATTER)

fun String.toStableLocalDate(): StableLocalDate =
    LocalDate.parse(substringBefore(" "), API_DATE_FORMATTER).toStable()
