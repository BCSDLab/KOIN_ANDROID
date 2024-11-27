package `in`.koreatech.koin.feature.timetable

import `in`.koreatech.koin.domain.model.timetable.response.TimetableLecture
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalTime

class TimetableLectureTest {
    private val dummyTimetableLecture = TimetableLecture(
        id = 1,
        lectureId = 1,
        code = "HRD011",
        classTitle = "직업능력개발훈련평가",
        professor = "우성민",
        grades = "2",
        lectureClass = "01",
        regularNumber = "40",
        department = "HRD학과",
        target = "전기3",
        classPlace = "",
        designScore = "0",
        classTime = emptyList()
    )

    @Test
    fun `0,1,100,101 주어질 때, listOf((월, list(9시,9시 30분)), (화, list(9시,9시 30분)))을 반환한다`() {
        val lecture = dummyTimetableLecture.copy(
            classTime = listOf(0, 1, 100, 101)
        )

        val formatDayOfWeekAndClassTime = lecture.findDayOfWeekAndLocalTime()

        assertEquals(
            formatDayOfWeekAndClassTime, listOf(
                DayOfWeek.MONDAY to listOf(LocalTime.of(9, 0), LocalTime.of(9, 30)),
                DayOfWeek.TUESDAY to listOf(LocalTime.of(9, 0), LocalTime.of(9, 30))
            )
        )
    }

    @Test
    fun `0,1,-1,100,101 주어질 때, listOf((월, list(9시,9시 30분)), (화, list(9시,9시 30분)))을 반환한다`() {
        val lecture = dummyTimetableLecture.copy(
            classTime = listOf(0, 1, -1, 100, 101)
        )

        val formatDayOfWeekAndClassTime = lecture.findDayOfWeekAndLocalTime()

        assertEquals(
            formatDayOfWeekAndClassTime, listOf(
                DayOfWeek.MONDAY to listOf(LocalTime.of(9, 0), LocalTime.of(9, 30)),
                DayOfWeek.TUESDAY to listOf(LocalTime.of(9, 0), LocalTime.of(9, 30))
            )
        )
    }

    @Test
    fun `0,1,-1,100,101,-1 주어질 때, listOf((월, list(9시,9시 30분)), (화, list(9시,9시 30분)))을 반환한다`() {
        val lecture = dummyTimetableLecture.copy(
            classTime = listOf(0, 1, -1, 100, 101,-1)
        )

        val formatDayOfWeekAndClassTime = lecture.findDayOfWeekAndLocalTime()

        assertEquals(
            formatDayOfWeekAndClassTime, listOf(
                DayOfWeek.MONDAY to listOf(LocalTime.of(9, 0), LocalTime.of(9, 30)),
                DayOfWeek.TUESDAY to listOf(LocalTime.of(9, 0), LocalTime.of(9, 30))
            )
        )
    }
}