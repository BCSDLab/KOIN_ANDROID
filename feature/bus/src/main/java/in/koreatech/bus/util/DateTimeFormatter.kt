package `in`.koreatech.bus.util

import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import `in`.koreatech.koin.core.util.shortNameRes
import `in`.koreatech.koin.feature.bus.R
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * 버스 조회 결과 뷰에서 쓰이는 날짜 포맷 String.
 *
 * 오늘, 내일 그리고 이후부터는 ex) 11월 27일(수)
 */
internal fun LocalDate.formatDateValue(resources: Resources): String {
    if (LocalDate.now() == this) {
        return resources.getString(R.string.today)
    }
    if (LocalDate.now().plusDays(1) == this) {
        return resources.getString(R.string.bus_tomorrow)
    }

    return resources.getString(
        R.string.bus_date_format,
        monthValue,
        dayOfMonth,
        resources.getString(dayOfWeek.shortNameRes())
    )
}

/** 버스 조회 결과 뷰에서 쓰이는 출발 시각 포맷 String.
 *
 * ex) 오늘 오전 10:30
 * ex) 11월 27일(수) 오후 3:30
 */
internal fun formatDepartureTime(
    date: String,
    daytime: String,
    hour: String,
    minute: String
): String {
    return "$date $daytime $hour:${minute.padStart(2, '0')}"
}

/**
 * [LocalDateTime]을 yyyy-MM-dd 형식으로 변환.
 *
 * ex) 2024-11-11
 */
internal fun LocalDateTime.formatUpdatedTime(): String {
    return this.format(
        DateTimeFormatter.ofPattern("yyyy-MM-dd")
    )
}

/**
 * [LocalTime]을 a hh:mm 형식으로 변환.
 *
 * ex) 오후 10:30
 */
internal fun LocalTime.formatTime(): String {
    return this.format(
        DateTimeFormatter.ofPattern("a hh:mm")
    )
}

/**
 * [LocalTime] 비교하여 지난 시간을 표시.
 *
 * ex) 14분 전
 * ex) 2시간 33분 전
 */
@Composable
internal fun LocalTime.formatBeforeTime(compareTo: LocalTime): String {
    val (hour, minute) =
        this.minusHours(compareTo.hour.toLong()).minusMinutes(compareTo.minute.toLong()).let {
            it.hour to it.minute
        }

    return if (hour == 0) {
        stringResource(R.string.bus_before_minute, minute)
    } else {
        stringResource(R.string.bus_before_hour_minute, hour, minute.coerceAtLeast(0))
    }
}

/**
 * [LocalDate]를 안내 기간으로 변환.
 *
 * ex) 2024년 9월 2일
 */
@Composable
internal fun LocalDate.formatPeriod(): String {
    return stringResource(R.string.bus_period_format, year, monthValue, dayOfMonth)
}
