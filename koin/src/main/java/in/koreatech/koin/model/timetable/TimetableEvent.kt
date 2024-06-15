package `in`.koreatech.koin.model.timetable

import androidx.compose.ui.graphics.Color
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalTime
import kotlin.math.ceil

data class TimetableEvent(
    val id: Int,
    val name: String,
    val color: Color,
    val dayOfWeek: DayOfWeek? = null,
    val start: LocalTime,
    val end: LocalTime,
    val description: String? = null,
) {
    fun dayOfWeekToKorean(): String =
        when (dayOfWeek) {
            DayOfWeek.MONDAY -> "월"
            DayOfWeek.TUESDAY -> "화"
            DayOfWeek.WEDNESDAY -> "수"
            DayOfWeek.THURSDAY -> "목"
            DayOfWeek.FRIDAY -> "금"
            DayOfWeek.SATURDAY -> "토"
            DayOfWeek.SUNDAY -> "일"
            else -> ""
        }

    fun convertToTimeBlock(endTime: LocalTime): TimeBlock {
        val startDuration = Duration.between(LocalTime.of(start.hour, 0), start).run {
            this.toMinutes() / 60f
        }
        val endDuration = Duration.between(start, endTime).run {
            this.toMinutes() / 60f
        }
        val duration = ceil(startDuration + endDuration)

        return TimeBlock(
            title = this.name,
            start = start,
            end = endTime,
            startDuration = startDuration,
            endDuration = endDuration,
            duration = duration,
            color = this.color
        )
    }

    fun convertToEmptyTimeBlock() = TimeBlock()
}
