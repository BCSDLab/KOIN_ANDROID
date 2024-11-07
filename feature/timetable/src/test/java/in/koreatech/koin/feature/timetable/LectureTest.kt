package `in`.koreatech.koin.feature.timetable

import `in`.koreatech.koin.domain.model.timetable.response.Lecture
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalTime

class LectureTest {
    private val lecture = Lecture(
        id = 1,
        code = "HRD011",
        name = "직업능력개발훈련평가",
        professor = "우성민",
        grades = "2",
        lectureClass = "01",
        regularNumber = "40",
        department = "HRD학과",
        target = "전기3",
        isEnglish = "",
        isElearning = "",
        designScore = "0",
        classTime = emptyList()
    )

    @Test
    fun `emptyList 주어질 때, emptyList을 반환한다`() {
        val lecture1 = lecture

        val result = lecture1.findDayOfWeekAndLocalTime() // when

        assertEquals(emptyList<Pair<DayOfWeek?, List<LocalTime>>>(), result)
    }

    @Test
    fun `0,1,100,101 주어질 때, {월 = 9시,9시 30분 | 화 = 9시,9시 30분}을 반환한다`() {
        val lecture1 = lecture.copy( // given
            classTime = listOf(0, 1, 100, 101)
        )

        val result = lecture1.findDayOfWeekAndLocalTime() // when

        assertEquals(
            listOf(
                Pair(DayOfWeek.MONDAY, listOf(LocalTime.of(9, 0), LocalTime.of(9, 30))),
                Pair(DayOfWeek.TUESDAY, listOf(LocalTime.of(9, 0), LocalTime.of(9, 30))),
            ),
            result
        )
    }

    @Test
    fun `200,201,210,211 주어질 때, {수=9시,9시30분 | 수 =14시,14시30분}을 반환한다`() {
        val lecture1 = lecture.copy( // given
            classTime = listOf(200, 201, 210, 211)
        )

        val result = lecture1.findDayOfWeekAndLocalTime() // when

        assertEquals(
            listOf(
                Pair(DayOfWeek.WEDNESDAY, listOf(LocalTime.of(9, 0), LocalTime.of(9, 30))),
                Pair(DayOfWeek.WEDNESDAY, listOf(LocalTime.of(14, 0), LocalTime.of(14, 30))),
            ),
            result
        )
    }
}