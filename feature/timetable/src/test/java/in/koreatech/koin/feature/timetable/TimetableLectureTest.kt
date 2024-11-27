package `in`.koreatech.koin.feature.timetable

import `in`.koreatech.koin.domain.model.timetable.response.TimetableLecture
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLectureClassInfo
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
        classInfos = emptyList(),
        designScore = "0",
    )

    @Test
    fun `강의 시간이 없을 때, 빈 리스트를 반환한다`() {
        val lecture = dummyTimetableLecture.copy(
            classInfos = emptyList()
        )

        val formatDayOfWeekAndClassTime = lecture.findDayOfWeekAndLocalTime()

        val actual: List<Pair<DayOfWeek?, List<LocalTime>>> = emptyList()
        assertEquals(formatDayOfWeekAndClassTime, actual)
    }

    @Test
    fun `강의 시간 0,1,100,101 일 때, 월요일 9시,9시30분, 화요일 9시,9시30분 반환된다`() {
        val lecture = dummyTimetableLecture.copy(
            classInfos = listOf(
                TimetableLectureClassInfo(
                    classTime = listOf(0, 1, 100, 101),
                    classPlace = ""
                )
            )
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
    fun `강의 시간 100,101,200,201 일 때, 화요일 9시,9시30분, 수요일 9시,9시30분 반환된다`() {
        val lecture = dummyTimetableLecture.copy(
            classInfos = listOf(
                TimetableLectureClassInfo(
                    classTime = listOf(100, 101),
                    classPlace = ""
                ),
                TimetableLectureClassInfo(
                    classTime = listOf(200, 201),
                    classPlace = ""
                ),
            )
        )

        val formatDayOfWeekAndClassTime = lecture.findDayOfWeekAndLocalTime()

        assertEquals(
            formatDayOfWeekAndClassTime, listOf(
                DayOfWeek.TUESDAY to listOf(LocalTime.of(9, 0), LocalTime.of(9, 30)),
                DayOfWeek.WEDNESDAY to listOf(LocalTime.of(9, 0), LocalTime.of(9, 30))
            )
        )
    }
}