package `in`.koreatech.koin.feature.timetable.state

import androidx.compose.ui.graphics.Color
import `in`.koreatech.koin.feature.timetable.model.TimetableColor
import `in`.koreatech.koin.feature.timetable.model.TimetableEvent
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.temporal.ChronoUnit

data class CustomExtraContentState(
    val id: Int = 0,
    val dayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
    val startTime: LocalTime = LocalTime.of(9, 0),
    val endTime: LocalTime = LocalTime.of(10, 0),
    val place: String = "",
    val isError: Boolean = false,
) {
    fun toTimetableEvent() = TimetableEvent(
        id = 0,
        lectureId = 0,
        name = "",
        color = TimetableColor(Color.White, Color.White),
        dayOfWeek = dayOfWeek,
        start = startTime,
        end = endTime
    )

    fun toClassTime(): List<Int> {
        val classTimes = mutableListOf<Int>()

        val dayOfWeekValue = when (dayOfWeek) {
            DayOfWeek.MONDAY -> 0
            DayOfWeek.TUESDAY -> 100
            DayOfWeek.WEDNESDAY -> 200
            DayOfWeek.THURSDAY -> 300
            DayOfWeek.FRIDAY -> 400
            else -> 0
        }

        val hourValue = (startTime.hour - 9) * 2
        val minutesValue = if (startTime.minute == 30) 1 else 0
        val timeValue = hourValue + minutesValue
        val minutes = if (endTime.hour == 23 && endTime.minute == 59) {
            (ChronoUnit.MINUTES.between(startTime, endTime)) + 1
        } else {
            (ChronoUnit.MINUTES.between(startTime, endTime))
        }

        repeat((minutes / 30).toInt()) {
            classTimes.add(dayOfWeekValue + (timeValue + it))
        }
        return classTimes
    }
}