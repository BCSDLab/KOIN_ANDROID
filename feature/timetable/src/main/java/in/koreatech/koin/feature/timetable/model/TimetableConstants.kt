package `in`.koreatech.koin.feature.timetable.model

import androidx.compose.ui.graphics.Color
import java.time.DayOfWeek
import java.time.LocalTime

object TimetableConstants {
    val days = listOf("월", "화", "수", "목", "금")
    const val eventHeight = 64
}

val dummyEvent = TimetableEvent(
    id = 1,
    name = "강의 제목",
    color = Color(0xFFAFBBF2),
    dayOfWeek = DayOfWeek.FRIDAY,
    start = LocalTime.of(16, 0),
    end = LocalTime.of(18, 0),
    description = "설명",
)