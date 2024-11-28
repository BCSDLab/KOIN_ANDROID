package `in`.koreatech.bus.util

import java.time.LocalDate
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