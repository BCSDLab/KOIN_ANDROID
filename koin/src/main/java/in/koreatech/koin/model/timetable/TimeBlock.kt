package `in`.koreatech.koin.model.timetable

import androidx.compose.ui.graphics.Color
import java.time.LocalTime

data class TimeBlock(
    val title: String = "",
    val start: LocalTime = LocalTime.of(0, 0),
    val end: LocalTime = LocalTime.of(0, 0),
    val startDuration: Float = 0f,
    val endDuration: Float = 0f,
    val duration: Float = 0f,
    val color: Color? = null,
)