package `in`.koreatech.koin.feature.recruitment.model

private const val API_DATE_SEPARATOR = "-"
private const val DISPLAY_DATE_SEPARATOR = "."
private const val DATE_TIME_DELIMITER = " "

fun String.toRecruitmentDisplayDate(): String =
    substringBefore(DATE_TIME_DELIMITER).replace(API_DATE_SEPARATOR, DISPLAY_DATE_SEPARATOR)
