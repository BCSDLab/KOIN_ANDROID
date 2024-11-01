package `in`.koreatech.koin.feature.timetable.model

import androidx.compose.ui.graphics.Color
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter

enum class DayOfWeekKorean(
    val koreanName: String
) {
    MONDAY("월"),
    TUESDAY("화"),
    WEDNESDAY("수"),
    THURSDAY("목"),
    FRIDAY("금"),
    SATURDAY("토"),
    SUNDAY("일")
}

data class TimetableEvent(
    val id: Int,
    val name: String,
    val color: Color,
    val dayOfWeek: DayOfWeek? = null,
    val start: LocalTime,
    val end: LocalTime,
    val description: String? = null,
) {
    /**
     * @test : TimetableEventTest 확인하기
     */
    fun formatClassTimeCode(): Pair<String, String> {
        val startPrefix = if("${start.hour - 8}".length == 1) "0${start.hour - 8}" else "${start.hour - 8}"
        val endPrefix = if("${end.hour - 8}".length == 1) "0${end.hour - 8}" else "${end.hour - 8}"
        val startSuffix = if ((start.minute == 0)) "A" else "B"
        val endSuffix = if ((end.minute == 0)) "A" else "B"

        return Pair(startPrefix+startSuffix, endPrefix +endSuffix)
    }

    fun dayOfWeekToKorean(): String =
        when (dayOfWeek) {
            DayOfWeek.MONDAY -> DayOfWeekKorean.MONDAY.koreanName
            DayOfWeek.TUESDAY -> DayOfWeekKorean.TUESDAY.koreanName
            DayOfWeek.WEDNESDAY -> DayOfWeekKorean.WEDNESDAY.koreanName
            DayOfWeek.THURSDAY -> DayOfWeekKorean.THURSDAY.koreanName
            DayOfWeek.FRIDAY -> DayOfWeekKorean.FRIDAY.koreanName
            DayOfWeek.SATURDAY -> DayOfWeekKorean.SATURDAY.koreanName
            DayOfWeek.SUNDAY -> DayOfWeekKorean.SUNDAY.koreanName
            else -> ""
        }
}
