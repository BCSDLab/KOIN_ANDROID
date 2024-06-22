package `in`.koreatech.koin.util.ext

import androidx.compose.ui.graphics.Color
import `in`.koreatech.koin.compose.ui.ColorPrimary
import `in`.koreatech.koin.compose.ui.basicColors
import `in`.koreatech.koin.compose.ui.defaultColors
import `in`.koreatech.koin.domain.model.timetable.Lecture
import `in`.koreatech.koin.model.timetable.TimetableEvent
import java.time.LocalTime

fun Lecture.toTimetableEvents(index: Int? = null, colors: List<Color>): List<TimetableEvent> {
    val events = mutableListOf<TimetableEvent>()
    /**
     * @input : {MONDAY=[09:00, 09:30], TUESDAY=[09:00, 09:30]}
     */
    findDayOfWeekAndTime().forEach { (key, value) ->
        val description = if (grades.length == 1) "0${grades} ${this.professor}" else "$grades $professor"
        val timetableEvent = TimetableEvent(
            id = id,
            name = name,
            color = colors[(if (index != null) index + 1 else 0) % colors.size],
            dayOfWeek = key,
            start = value.firstOrNull() ?: LocalTime.of(0, 0),
            end = value.lastOrNull()?.plusMinutes(30) ?: LocalTime.of(0, 0),
            description = description
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
