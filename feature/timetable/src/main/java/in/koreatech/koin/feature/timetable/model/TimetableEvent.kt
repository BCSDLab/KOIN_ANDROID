package `in`.koreatech.koin.feature.timetable.model

import androidx.compose.ui.graphics.Color
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val timetableEventTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

data class TimetableEvent(
    val id: Int,
    val name: String,
    val color: Color,
    val dayOfWeek: DayOfWeek? = null,
    val start: LocalTime,
    val end: LocalTime,
    val description: String? = null,
) {
    fun timeDashText(): String =
        "${start.format(timetableEventTimeFormatter)} - ${end.format(timetableEventTimeFormatter)}"
}
