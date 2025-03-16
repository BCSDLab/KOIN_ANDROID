package `in`.koreatech.koin.feature.timetable.state

import androidx.compose.ui.graphics.Color
import `in`.koreatech.koin.domain.model.timetable.response.Lecture
import `in`.koreatech.koin.feature.timetable.model.TimetableColor
import `in`.koreatech.koin.feature.timetable.model.TimetableEvent
import java.time.DayOfWeek
import java.time.LocalTime
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class CustomContentState(
    val schedule: String = "",
    val professor: String = "",
    val isScheduleError: Boolean = false,
    val data: ImmutableList<CustomExtraContentState> = persistentListOf(CustomExtraContentState())
) {
    fun toTimetableEvent() =
        TimetableEvent(
            id = 0,
            lectureId = 0,
            name = "",
            color = TimetableColor(Color.White, Color.White),
            dayOfWeek = DayOfWeek.MONDAY,
            start = LocalTime.of(9, 0),
            end = LocalTime.of(10, 0)
        )

    fun toLectures(): List<Lecture> {
        val lectures = mutableListOf<Lecture>()

        data.forEach { lecture ->
            lectures.add(
                Lecture(
                    id = 0,
                    name = schedule.trim(),
                    professor = professor.trim(),
                    classTime = lecture.toClassTime(),
                    place = lecture.place.trim()
                )
            )
        }

        return lectures
    }

    fun formatTimeRange(): Int {
        val endTime = data.maxOf { it.endTime }
        val hour = endTime.hour
        val minutes = endTime.minute

        return when (hour) {
            in 9..17 -> 9
            18 -> if (minutes == 0) 9 else 10
            19 -> if (minutes == 0) 10 else 11
            20 -> if (minutes == 0) 11 else 12
            21 -> if (minutes == 0) 12 else 13
            22 -> if (minutes == 0) 13 else 14
            23 -> if (minutes == 0) 14 else 15
            else -> 10
        }
    }
}
