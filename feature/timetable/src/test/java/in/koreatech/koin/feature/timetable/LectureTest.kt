package `in`.koreatech.koin.feature.timetable

import `in`.koreatech.koin.domain.model.timetable.response.Lecture
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalTime

class LectureTest {
    private val dummpyLecture =
        Lecture(
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
            classTime = emptyList(),
        )

    @Test
    fun `강의 시간이 없을 때, 빈 리스트를 반환한다`() {
        val lecture =
            dummpyLecture.copy(
                classTime = emptyList(),
            )

        val result = lecture.findDayOfWeekAndLocalTime() // when

        assertEquals(emptyList<Pair<DayOfWeek?, List<LocalTime>>>(), result)
    }

    @Test
    fun `강의 시간이 0,1,100,101 일 때, 월요일 9시,9시30분, 화요일 9시,9시30분을 반환한다`() {
        val lecture =
            dummpyLecture.copy( // given
                classTime = listOf(0, 1, 100, 101),
            )

        val result = lecture.findDayOfWeekAndLocalTime() // when

        assertEquals(
            listOf(
                Pair(DayOfWeek.MONDAY, listOf(LocalTime.of(9, 0), LocalTime.of(9, 30))),
                Pair(DayOfWeek.TUESDAY, listOf(LocalTime.of(9, 0), LocalTime.of(9, 30))),
            ),
            result,
        )
    }

    @Test
    fun `강의 시간이 200,201,210,211 일 때, 수요일 9시,9시30분, 수요일 14시,14시30분을 반환한다`() {
        val lecture =
            dummpyLecture.copy( // given
                classTime = listOf(200, 201, 210, 211),
            )

        val result = lecture.findDayOfWeekAndLocalTime() // when

        assertEquals(
            listOf(
                Pair(DayOfWeek.WEDNESDAY, listOf(LocalTime.of(9, 0), LocalTime.of(9, 30))),
                Pair(DayOfWeek.WEDNESDAY, listOf(LocalTime.of(14, 0), LocalTime.of(14, 30))),
            ),
            result,
        )
    }
}
