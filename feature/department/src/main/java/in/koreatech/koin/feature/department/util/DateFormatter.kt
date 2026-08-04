package `in`.koreatech.koin.feature.department.util

import java.time.format.DateTimeFormatter
import java.util.Locale

internal val DEPARTMENT_UPDATED_AT_FORMATTER: DateTimeFormatter =
    DateTimeFormatter.ofPattern("yyyy.MM.dd", Locale.KOREA)
