package `in`.koreatech.koin.feature.timetable

import androidx.compose.ui.graphics.Color
import `in`.koreatech.koin.feature.timetable.model.TimetableColor
import `in`.koreatech.koin.feature.timetable.model.TimetableEvent
import java.time.DayOfWeek
import java.time.LocalTime
import org.junit.Assert.assertEquals
import org.junit.Test

class TimetableEventTest {
    private val sampleTimetableEvent =
        TimetableEvent( // given
            id = 1,
            lectureId = 1,
            name = "강의 제목",
            color = TimetableColor(Color(0xFFAFBBF2), Color(0xFFAFBBF2)),
            dayOfWeek = DayOfWeek.FRIDAY,
            start = LocalTime.of(9, 30),
            end = LocalTime.of(10, 30),
            description = "설명"
        )

    @Test
    fun `강의 시간이 9시,10시일 때, 01A,02A 반환`() {
        val event =
            sampleTimetableEvent.copy( // given
                start = LocalTime.of(9, 0),
                end = LocalTime.of(10, 0)
            )

        val code = event.formatClassTimeCode() // when

        assertEquals(code, Pair("01A", "02A")) // then
    }

    @Test
    fun `강의 시간이 9시,10시30분일 때, 01A,02B 반환`() {
        val event =
            sampleTimetableEvent.copy( // given
                start = LocalTime.of(9, 0),
                end = LocalTime.of(10, 30)
            )

        val code = event.formatClassTimeCode() // when

        assertEquals(code, Pair("01A", "02B")) // then
    }

    @Test
    fun `강의 시간이 9시30분,10시일 때, 01B,02A 반환`() {
        val event =
            sampleTimetableEvent.copy( // given
                start = LocalTime.of(9, 30),
                end = LocalTime.of(10, 0)
            )

        val code = event.formatClassTimeCode() // when

        assertEquals(code, Pair("01B", "02A")) // then
    }

    @Test
    fun `강의 시간이 9시30분,10시30분일 때, 01B,02B 반환`() {
        val event =
            sampleTimetableEvent.copy( // given
                start = LocalTime.of(9, 30),
                end = LocalTime.of(10, 30)
            )

        val code = event.formatClassTimeCode() // when

        assertEquals(code, Pair("01B", "02B")) // then
    }
}
