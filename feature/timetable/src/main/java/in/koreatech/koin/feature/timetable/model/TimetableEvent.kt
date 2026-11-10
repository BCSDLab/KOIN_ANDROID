package `in`.koreatech.koin.feature.timetable.model

import androidx.compose.ui.graphics.Color
import java.time.DayOfWeek
import java.time.LocalTime

data class TimetableColor(
    val header: Color,
    val content: Color
)

data class TimetableEvent(
    val id: Int,
    val lectureId: Int,
    val name: String,
    val place: String = "",
    val professor: String = "",
    val color: TimetableColor,
    val dayOfWeek: DayOfWeek? = null,
    val start: LocalTime,
    val end: LocalTime,
    val description: String? = null
) {
    /**
     * @test : TimetableEventTest.kt
     */
    fun formatClassTimeCode(): Pair<String, String> {
        val startPrefix = if ("${start.hour - 8}".length == 1) "0${start.hour - 8}" else "${start.hour - 8}"
        val endPrefix = if ("${end.hour - 8}".length == 1) "0${end.hour - 8}" else "${end.hour - 8}"
        val startSuffix = if ((start.minute == 0)) "A" else "B"
        val endSuffix = if ((end.minute == 0)) "A" else "B"

        return Pair(startPrefix + startSuffix, endPrefix + endSuffix)
    }
}
