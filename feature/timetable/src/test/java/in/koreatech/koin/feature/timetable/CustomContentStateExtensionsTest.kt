package `in`.koreatech.koin.feature.timetable

import androidx.compose.ui.graphics.Color
import `in`.koreatech.koin.feature.timetable.model.TimetableColor
import `in`.koreatech.koin.feature.timetable.model.TimetableEvent
import `in`.koreatech.koin.feature.timetable.state.CustomContentState
import `in`.koreatech.koin.feature.timetable.state.CustomExtraContentState
import `in`.koreatech.koin.feature.timetable.utils.duplicationByTimeTableEvents
import `in`.koreatech.koin.feature.timetable.utils.isValidationPlace
import `in`.koreatech.koin.feature.timetable.utils.permuteDuplicationLecture
import java.time.DayOfWeek
import java.time.LocalTime
import kotlinx.collections.immutable.persistentListOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class CustomContentStateExtensionsTest {
    private fun extraContent(
        id: Int = 0,
        dayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
        start: LocalTime,
        end: LocalTime
    ) = CustomExtraContentState(
        id = id,
        dayOfWeek = dayOfWeek,
        startTime = start,
        endTime = end
    )

    private fun stateOf(vararg contents: CustomExtraContentState) =
        CustomContentState(data = persistentListOf(*contents))

    private fun event(
        id: Int = 1,
        dayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
        start: LocalTime,
        end: LocalTime
    ) = TimetableEvent(
        id = id,
        lectureId = id,
        name = "강의 $id",
        color = TimetableColor(Color.White, Color.White),
        dayOfWeek = dayOfWeek,
        start = start,
        end = end
    )

    @Test
    fun `같은 시간 구간의 강의는 중복이고 뒤 항목만 오류로 표시된다`() {
        val first = extraContent(id = 1, start = LocalTime.of(9, 0), end = LocalTime.of(10, 0))
        val second = extraContent(id = 2, start = LocalTime.of(9, 0), end = LocalTime.of(10, 0))

        val result = stateOf(first, second).permuteDuplicationLecture()

        assertEquals(listOf(first, second.copy(isError = true)), result)
    }

    @Test
    fun `이전 강의가 다음 강의를 완전히 포함하면 중복이다`() {
        val outer = extraContent(id = 1, start = LocalTime.of(9, 0), end = LocalTime.of(12, 0))
        val inner = extraContent(id = 2, start = LocalTime.of(10, 0), end = LocalTime.of(11, 0))

        val result = stateOf(outer, inner).permuteDuplicationLecture()

        assertEquals(listOf(outer, inner.copy(isError = true)), result)
    }

    @Test
    fun `다음 강의가 이전 강의를 완전히 포함하면 중복이다`() {
        val inner = extraContent(id = 1, start = LocalTime.of(10, 0), end = LocalTime.of(11, 0))
        val outer = extraContent(id = 2, start = LocalTime.of(9, 0), end = LocalTime.of(12, 0))

        val result = stateOf(inner, outer).permuteDuplicationLecture()

        assertEquals(listOf(inner, outer.copy(isError = true)), result)
    }

    @Test
    fun `일부만 겹치는 강의는 중복이다`() {
        val first = extraContent(id = 1, start = LocalTime.of(9, 0), end = LocalTime.of(10, 30))
        val second = extraContent(id = 2, start = LocalTime.of(10, 0), end = LocalTime.of(11, 0))

        val result = stateOf(first, second).permuteDuplicationLecture()

        assertEquals(listOf(first, second.copy(isError = true)), result)
    }

    @Test
    fun `일부만 겹치는 강의는 순서가 바뀌어도 중복이다`() {
        val first = extraContent(id = 1, start = LocalTime.of(10, 0), end = LocalTime.of(11, 0))
        val second = extraContent(id = 2, start = LocalTime.of(9, 0), end = LocalTime.of(10, 30))

        val result = stateOf(first, second).permuteDuplicationLecture()

        assertEquals(listOf(first, second.copy(isError = true)), result)
    }

    @Test
    fun `끝나는 시각과 시작하는 시각이 맞닿은 강의는 중복이 아니다`() {
        val first = extraContent(id = 1, start = LocalTime.of(9, 0), end = LocalTime.of(10, 0))
        val second = extraContent(id = 2, start = LocalTime.of(10, 0), end = LocalTime.of(11, 0))

        assertNull(stateOf(first, second).permuteDuplicationLecture())
        assertNull(stateOf(second, first).permuteDuplicationLecture())
    }

    @Test
    fun `요일이 다른 강의는 시간이 같아도 중복이 아니다`() {
        val monday = extraContent(id = 1, dayOfWeek = DayOfWeek.MONDAY, start = LocalTime.of(9, 0), end = LocalTime.of(10, 0))
        val tuesday = extraContent(id = 2, dayOfWeek = DayOfWeek.TUESDAY, start = LocalTime.of(9, 0), end = LocalTime.of(10, 0))

        assertNull(stateOf(monday, tuesday).permuteDuplicationLecture())
    }

    @Test
    fun `기존 시간표 강의가 직접 추가 강의를 완전히 포함하면 해당 이벤트를 반환한다`() {
        val timetableEvent = event(id = 7, start = LocalTime.of(9, 0), end = LocalTime.of(12, 0))
        val content = extraContent(start = LocalTime.of(10, 0), end = LocalTime.of(11, 0))

        val result = stateOf(content).duplicationByTimeTableEvents(listOf(timetableEvent))

        assertSame(timetableEvent, result)
    }

    @Test
    fun `직접 추가 강의가 기존 시간표 강의를 완전히 포함해도 해당 이벤트를 반환한다`() {
        val timetableEvent = event(id = 7, start = LocalTime.of(10, 0), end = LocalTime.of(11, 0))
        val content = extraContent(start = LocalTime.of(9, 0), end = LocalTime.of(12, 0))

        val result = stateOf(content).duplicationByTimeTableEvents(listOf(timetableEvent))

        assertSame(timetableEvent, result)
    }

    @Test
    fun `기존 시간표 강의와 일부만 겹치면 해당 이벤트를 반환한다`() {
        val timetableEvent = event(id = 7, start = LocalTime.of(10, 0), end = LocalTime.of(11, 0))
        val content = extraContent(start = LocalTime.of(9, 0), end = LocalTime.of(10, 30))

        assertSame(timetableEvent, stateOf(content).duplicationByTimeTableEvents(listOf(timetableEvent)))
    }

    @Test
    fun `기존 시간표 강의와 맞닿기만 하면 중복이 아니다`() {
        val timetableEvent = event(id = 7, start = LocalTime.of(9, 0), end = LocalTime.of(10, 0))
        val content = extraContent(start = LocalTime.of(10, 0), end = LocalTime.of(11, 0))

        assertNull(stateOf(content).duplicationByTimeTableEvents(listOf(timetableEvent)))
    }

    @Test
    fun `요일이 다른 기존 시간표 강의는 중복이 아니다`() {
        val timetableEvent = event(id = 7, dayOfWeek = DayOfWeek.TUESDAY, start = LocalTime.of(9, 0), end = LocalTime.of(10, 0))
        val content = extraContent(dayOfWeek = DayOfWeek.MONDAY, start = LocalTime.of(9, 0), end = LocalTime.of(10, 0))

        assertNull(stateOf(content).duplicationByTimeTableEvents(listOf(timetableEvent)))
    }

    @Test
    fun `여러 기존 시간표 강의 중 처음으로 겹치는 이벤트를 반환한다`() {
        val notOverlapping = event(id = 1, start = LocalTime.of(9, 0), end = LocalTime.of(10, 0))
        val overlapping = event(id = 2, start = LocalTime.of(11, 0), end = LocalTime.of(12, 0))
        val content = extraContent(start = LocalTime.of(11, 30), end = LocalTime.of(13, 0))

        val result = stateOf(content).duplicationByTimeTableEvents(listOf(notOverlapping, overlapping))

        assertSame(overlapping, result)
    }

    @Test
    fun `시간표 이벤트가 없으면 null을 반환한다`() {
        val content = extraContent(start = LocalTime.of(9, 0), end = LocalTime.of(10, 0))

        assertNull(stateOf(content).duplicationByTimeTableEvents(emptyList()))
    }

    @Test
    fun `중복이 없는 시간표 이벤트 목록에서는 null을 반환한다`() {
        val events =
            listOf(
                event(id = 1, dayOfWeek = DayOfWeek.MONDAY, start = LocalTime.of(9, 0), end = LocalTime.of(10, 0)),
                event(id = 2, dayOfWeek = DayOfWeek.TUESDAY, start = LocalTime.of(9, 0), end = LocalTime.of(10, 0))
            )
        val content = extraContent(dayOfWeek = DayOfWeek.MONDAY, start = LocalTime.of(10, 0), end = LocalTime.of(11, 0))

        assertNull(stateOf(content).duplicationByTimeTableEvents(events))
    }

    @Test
    fun `장소에 쉼표가 있으면 검증에 실패한다`() {
        val customContent =
            CustomExtraContentState(
                place = "1공학관, 2공학관",
                startTime = LocalTime.of(9, 0),
                endTime = LocalTime.of(10, 0)
            )

        assertFalse(stateOf(customContent).isValidationPlace())
    }

    @Test
    fun `장소에 쉼표가 없으면 검증에 성공한다`() {
        val customContent =
            CustomExtraContentState(
                place = "1공학관 101호",
                startTime = LocalTime.of(9, 0),
                endTime = LocalTime.of(10, 0)
            )

        assertTrue(stateOf(customContent).isValidationPlace())
    }
}
