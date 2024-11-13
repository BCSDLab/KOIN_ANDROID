package `in`.koreatech.koin.feature.timetable.utils

import androidx.compose.ui.graphics.Color
import `in`.koreatech.koin.domain.model.timetable.response.Lecture
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLecture
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLectures
import `in`.koreatech.koin.feature.timetable.model.TimetableColor
import `in`.koreatech.koin.feature.timetable.model.TimetableEvent
import `in`.koreatech.koin.feature.timetable.model.defaultColors
import java.time.LocalTime

fun TimetableLectures.getTimetableEvents() =
    timetable.flatMapIndexed { index, timetableLecture -> timetableLecture.toTimetableEvents(index) }

fun TimetableLecture.toTimetableEvents(index: Int): List<TimetableEvent> {
    val events = mutableListOf<TimetableEvent>()
    /**
     * @input : {MONDAY=[09:00, 09:30], TUESDAY=[09:00, 09:30]}
     */
    findDayOfWeekAndLocalTime().forEach { (key, value) ->
        val timetableEvent = TimetableEvent(
            id = id,
            lectureId = lectureId,
            name = classTitle,
            professor = professor,
            place = classPlace,
            color = defaultColors[index % defaultColors.size],
            dayOfWeek = key,
            start = value.firstOrNull() ?: return@forEach,
            end = if (value.lastOrNull() == LocalTime.of(23, 30)) {
                LocalTime.of(23, 59)
            } else {
                value.lastOrNull()?.plusMinutes(30) ?: return@forEach
            },
            description = ""
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
    findDayOfWeekAndLocalTime().forEach { (key, value) ->
        val timetableEvent = TimetableEvent(
            id = id,
            lectureId = 1, // 상관 없음
            name = name,
            professor = professor,
            place = place ?: "",
            color = TimetableColor(Color(0xFFFFFF), Color(0xFFFFFF)),
            dayOfWeek = key,
            start = value.firstOrNull() ?: LocalTime.of(0, 0),
            end = if (value.lastOrNull() == LocalTime.of(23, 30)) {
                LocalTime.of(23, 59)
            } else {
                value.lastOrNull()?.plusMinutes(30) ?: return@forEach
            },
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

fun List<TimetableEvent>.formatTimeRange(): Int {
    val maxEndEvent = this.maxBy { it.end }
    return if (maxEndEvent.end.hour >= 18) {
        if (maxEndEvent.end.minute == 30 || maxEndEvent.end.minute == 59) {
            (maxEndEvent.end.hour - 9) + 1
        } else {
            (maxEndEvent.end.hour - 9)
        }
    } else 9
}