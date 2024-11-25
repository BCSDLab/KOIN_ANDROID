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


    // place 구분자 : ,
    // classTimes 구분자 : -1
    fun toLectures(): List<Lecture> {
        val lectures = mutableListOf<Lecture>()

        var places = ""
        val classTimes = mutableListOf<Int>()
        repeat(data.size + 1) { index ->
            if (index == 0) {
                places += data[0].place
                classTimes.addAll(data[0].toClassTime())
            } else {
                places += ", ${data[index -1].place}"
                classTimes.addAll(data[index - 1].toClassTime())
            }
            classTimes.add(-1)
        }
        Lecture(
            id = 0,
            name = schedule.trim(),
            professor = professor.trim(),
            classTime = classTimes,
            place = places.trim()
        ).let { lectures.add(it) }

        return lectures
    }
}
