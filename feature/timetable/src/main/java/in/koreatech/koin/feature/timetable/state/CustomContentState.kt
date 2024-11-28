package `in`.koreatech.koin.feature.timetable.state

import androidx.compose.ui.graphics.Color
import `in`.koreatech.koin.domain.model.timetable.response.Lecture
import `in`.koreatech.koin.feature.timetable.model.TimetableColor
import `in`.koreatech.koin.feature.timetable.model.TimetableEvent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.DayOfWeek
import java.time.LocalTime

data class CustomContentState(
    val schedule: String = "",
    val professor: String = "",
    val isScheduleError: Boolean = false,
    val data: ImmutableList<CustomExtraContentState> = persistentListOf(CustomExtraContentState())
) {
    fun toTimetableEvent() = TimetableEvent(
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
}
