package `in`.koreatech.bus.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * 버스 조회 결과 뷰에서 쓰이는 날짜 포맷 String.
 *
 * 오늘, 내일 그리고 이후부터는 ex) 11월 27일(수)
 */
internal fun LocalDate.formatDateValue(): String {
    if (LocalDate.now() == this)
        return "오늘"
    if (LocalDate.now().plusDays(1) == this)
        return "내일"

    return this.format(
        DateTimeFormatter.ofPattern("M월 d일(E)", Locale.KOREA)
    ).replace("요일", "")
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
    return "$date $daytime ${hour}:${minute.padStart(2, '0')}"
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
internal fun LocalTime.formatBeforeTime(compareTo: LocalTime): String {
    val (hour, minute) = this.minusHours(compareTo.hour.toLong()).minusMinutes(compareTo.minute.toLong()).let {
        it.hour to it.minute
    }

    return if (hour == 0)
        "${minute}분 전"
    else
        "${hour}시간 ${minute.coerceAtLeast(0)}분 전"
}

/**
 * [LocalDate]를 안내 기간으로 변환.
 *
 * ex) 2024년 9월 2일
 */
internal fun LocalDate.formatPeriod(): String {
    return this.format(
        DateTimeFormatter.ofPattern("yyyy년 M월 d일")
    )
}