package `in`.koreatech.koin.feature.timetable.utils

import androidx.compose.ui.graphics.Color
import `in`.koreatech.koin.domain.model.timetable.response.Lecture
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLecture
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLectures
import `in`.koreatech.koin.feature.timetable.model.SemesterModel
import `in`.koreatech.koin.feature.timetable.model.SemesterType
import `in`.koreatech.koin.feature.timetable.model.TimetableColor
import `in`.koreatech.koin.feature.timetable.model.TimetableEvent
import `in`.koreatech.koin.feature.timetable.model.defaultColors
import java.time.LocalTime

fun TimetableLectures.getTimetableEvents() = timetable.flatMapIndexed { index, timetableLecture -> timetableLecture.toTimetableEvents(index) }

fun TimetableLecture.toTimetableEvents(index: Int): List<TimetableEvent> {
    val events = mutableListOf<TimetableEvent>()
    /**
     * @input : {MONDAY=[09:00, 09:30], TUESDAY=[09:00, 09:30]}
     */
    findDayOfWeekAndTime().forEach { (key, value) ->
        val timetableEvent = TimetableEvent(
            id = id,
            lectureId = lectureId,
            name = classTitle,
            color = defaultColors[index % defaultColors.size],
            dayOfWeek = key,
            start = value.firstOrNull() ?: LocalTime.of(0, 0),
            end = value.lastOrNull()?.plusMinutes(30) ?: LocalTime.of(0, 0),
            description = professor
        )
        events.add(timetableEvent)
    }
    /**
     * @output :
     * [
     *  TimetableEvent(0, "강의이름1", 색상1, MONDAY, 09:00, 09:30, null),
     *  TimetableEvent(0, "강의이름2", 색상2, TUESDAY, 09:00, 09:30, null),
     * ]
     */
    return events
}

fun Lecture.toTimetableEvents(): List<TimetableEvent> {
    val events = mutableListOf<TimetableEvent>()
    /**
     * @input : {MONDAY=[09:00, 09:30], TUESDAY=[09:00, 09:30]}
     */
    findDayOfWeekAndTime().forEach { (key, value) ->
        val timetableEvent = TimetableEvent(
            id = id,
            lectureId = 1, // 상관 없음
            name = name,
            color = TimetableColor(Color(0xFFFFFF), Color(0xFFFFFF)),
            dayOfWeek = key,
            start = value.firstOrNull() ?: LocalTime.of(0, 0),
            end = value.lastOrNull()?.plusMinutes(30) ?: LocalTime.of(0, 0),
            description = professor
        )
        events.add(timetableEvent)
    }
    /**
     * @output :
     * [
     *  TimetableEvent(0, "강의이름1", 색상1, MONDAY, 09:00, 09:30, null),
     *  TimetableEvent(0, "강의이름2", 색상2, TUESDAY, 09:00, 09:30, null),
     * ]
     */
    return events
}

// TODO::UseCase 에서 변환하는게 좋아보이네
fun String.toSemesterModel(): SemesterModel = SemesterModel(
    year = substring(0, 4).toInt(),
    type = substring(4).toSemesterType()
)

fun String.toSemesterType(): SemesterType = when (this) {
    "1" -> SemesterType.Spring
    "-여름" -> SemesterType.Summer
    "2" -> SemesterType.Fall
    "-겨울" -> SemesterType.Winter
    else -> throw (Exception("\"$this\""))
}