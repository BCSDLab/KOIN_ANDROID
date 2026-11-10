package `in`.koreatech.koin.core.util

import androidx.annotation.StringRes
import `in`.koreatech.koin.core.R
import java.time.DayOfWeek

/** 요일 약어 문자열 리소스 (월, 화, 수 ...) */
@StringRes
fun DayOfWeek.shortNameRes(): Int = when (this) {
    DayOfWeek.MONDAY -> R.string.day_of_week_monday_short
    DayOfWeek.TUESDAY -> R.string.day_of_week_tuesday_short
    DayOfWeek.WEDNESDAY -> R.string.day_of_week_wednesday_short
    DayOfWeek.THURSDAY -> R.string.day_of_week_thursday_short
    DayOfWeek.FRIDAY -> R.string.day_of_week_friday_short
    DayOfWeek.SATURDAY -> R.string.day_of_week_saturday_short
    DayOfWeek.SUNDAY -> R.string.day_of_week_sunday_short
}

/** 요일 전체 이름 문자열 리소스 (월요일, 화요일, 수요일 ...) */
@StringRes
fun DayOfWeek.fullNameRes(): Int = when (this) {
    DayOfWeek.MONDAY -> R.string.day_of_week_monday_full
    DayOfWeek.TUESDAY -> R.string.day_of_week_tuesday_full
    DayOfWeek.WEDNESDAY -> R.string.day_of_week_wednesday_full
    DayOfWeek.THURSDAY -> R.string.day_of_week_thursday_full
    DayOfWeek.FRIDAY -> R.string.day_of_week_friday_full
    DayOfWeek.SATURDAY -> R.string.day_of_week_saturday_full
    DayOfWeek.SUNDAY -> R.string.day_of_week_sunday_full
}

/** 서버가 내려주는 영문 요일 값("MONDAY" 등)을 [DayOfWeek]로 변환한다. 알 수 없는 값이면 null. */
fun String.toDayOfWeekOrNull(): DayOfWeek? = runCatching { DayOfWeek.valueOf(this) }.getOrNull()
