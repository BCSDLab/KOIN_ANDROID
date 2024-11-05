package `in`.koreatech.koin.feature.timetable

import androidx.compose.ui.graphics.Color
import `in`.koreatech.koin.feature.timetable.model.TimetableColor
import `in`.koreatech.koin.feature.timetable.model.TimetableEvent
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalTime

class TimetableEventTest {
    val sampleEvent = TimetableEvent( // given
        id = 1,
        name = "강의 제목",
        color = TimetableColor(Color(0xFFAFBBF2),Color(0xFFAFBBF2)),
        dayOfWeek = DayOfWeek.FRIDAY,
        start = LocalTime.of(9, 30),
        end = LocalTime.of(10, 30),
        description = "설명",
    )


    @Test
    fun `LocalTime 9시 ~ 10시 시간 코드로 변환`() {
        val event = sampleEvent.copy( // given
            start = LocalTime.of(9, 0),
            end = LocalTime.of(10, 0)
        )

        val code = event.formatClassTimeCode()  // when

        assertEquals(code, Pair("01A", "02A")) // then
    }


    @Test
    fun `LocalTime 9시 ~ 10시 30분 시간 코드로 변환`() {
        val event = sampleEvent.copy( // given
            start = LocalTime.of(9, 0),
            end = LocalTime.of(10, 30)
        )

        val code = event.formatClassTimeCode()  // when

        assertEquals(code, Pair("01A", "02B")) // then
    }

    @Test
    fun `LocalTime 9시 30분 ~ 10시 시간 코드로 변환`() {
        val event = sampleEvent.copy( // given
            start = LocalTime.of(9, 30),
            end = LocalTime.of(10, 0)
        )

        val code = event.formatClassTimeCode()  // when

        assertEquals(code, Pair("01B", "02A")) // then
    }


    @Test
    fun `LocalTime 9시 30분 ~ 10시 30분 시간 코드로 변환`() {
        val event = sampleEvent.copy( // given
            start = LocalTime.of(9, 30),
            end = LocalTime.of(10, 30)
        )

        val code = event.formatClassTimeCode()  // when

        assertEquals(code, Pair("01B", "02B")) // then
    }
}