package `in`.koreatech.koin.feature.timetable

import `in`.koreatech.koin.domain.model.timetable.response.TimetableLecture
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLectures
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class TimetableLecturesTest {
    val dummyTimetableLecture = TimetableLecture(
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
        classTime = listOf(
            0,
        )
    )
    val dummyTimetables = TimetableLectures(
        timetableFrameId = 1,
        timetable = listOf(dummyTimetableLecture),
        grades = 1,
        totalGrades = 1
    )

    @Test
    fun `classTime이 emptyList() 로 주어졌을 때, 화면에 보이는 시간 범위 9(9시~18시)를 반환한다`() = runTest {
        val timetables = dummyTimetables.copy(
            timetable = listOf(
                dummyTimetableLecture.copy(classTime = emptyList())
            )
        ) // given

        val range = timetables.formatTimeRange()

        assertEquals(range, 9)
    }

    @Test
    fun `classTime이 0~17로 주어졌을 때, 화면에 보이는 시간 범위 9(9시~18시)를 반환한다`() = runTest {
        val timetables = dummyTimetables.copy(
            timetable = listOf(
                dummyTimetableLecture.copy(classTime = listOf(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17))
            )
        ) // given

        val range = timetables.formatTimeRange()

        assertEquals(range, 9)
    }

    @Test
    fun `classTime이 18과 19로 주어졌을 때, 화면에 보이는 시간 범위 10(9시~19시)을 반환한다`() = runTest {
        val t1 = dummyTimetables.copy(timetable = listOf(dummyTimetableLecture.copy(classTime = listOf(18)))) // given

        val range1 = t1.formatTimeRange()

        assertEquals(range1, 10)

        val t2= dummyTimetables.copy(timetable = listOf(dummyTimetableLecture.copy(classTime = listOf(19)))) // given

        val range2 = t2.formatTimeRange()

        assertEquals(range2, 10)
    }

    @Test
    fun `classTime이 20과 21로 주어졌을 때, 화면에 보이는 시간 범위 11(9시~20시)을 반환한다`() = runTest {
        val t1 = dummyTimetables.copy(timetable = listOf(dummyTimetableLecture.copy(classTime = listOf(20)))) // given

        val range1 = t1.formatTimeRange()

        assertEquals(range1, 11)

        val t2= dummyTimetables.copy(timetable = listOf(dummyTimetableLecture.copy(classTime = listOf(21)))) // given

        val range2 = t2.formatTimeRange()

        assertEquals(range2, 11)
    }

    @Test
    fun `classTime이 22과 23로 주어졌을 때, 화면에 보이는 시간 범위 12(9시~21시)을 반환한다`() = runTest {
        val t1 = dummyTimetables.copy(timetable = listOf(dummyTimetableLecture.copy(classTime = listOf(22)))) // given

        val range1 = t1.formatTimeRange()

        assertEquals(range1, 12)

        val t2= dummyTimetables.copy(timetable = listOf(dummyTimetableLecture.copy(classTime = listOf(23)))) // given

        val range2 = t2.formatTimeRange()

        assertEquals(range2, 12)
    }

    @Test
    fun `classTime이 24과 25로 주어졌을 때, 화면에 보이는 시간 범위 13(9시~22시)을 반환한다`() = runTest {
        val t1 = dummyTimetables.copy(timetable = listOf(dummyTimetableLecture.copy(classTime = listOf(24)))) // given

        val range1 = t1.formatTimeRange()

        assertEquals(range1, 13)

        val t2= dummyTimetables.copy(timetable = listOf(dummyTimetableLecture.copy(classTime = listOf(25)))) // given

        val range2 = t2.formatTimeRange()

        assertEquals(range2, 13)
    }

    @Test
    fun `classTime이 26과 27로 주어졌을 때, 화면에 보이는 시간 범위 14(9시~23시)을 반환한다`() = runTest {
        val t1 = dummyTimetables.copy(timetable = listOf(dummyTimetableLecture.copy(classTime = listOf(26)))) // given

        val range1 = t1.formatTimeRange()

        assertEquals(range1, 14)

        val t2= dummyTimetables.copy(timetable = listOf(dummyTimetableLecture.copy(classTime = listOf(27)))) // given

        val range2 = t2.formatTimeRange()

        assertEquals(range2, 14)
    }

    @Test
    fun `classTime이 28과 29로 주어졌을 때, 화면에 보이는 시간 범위 15(9시~24시)을 반환한다`() = runTest {
        val t1 = dummyTimetables.copy(timetable = listOf(dummyTimetableLecture.copy(classTime = listOf(28)))) // given

        val range1 = t1.formatTimeRange()

        assertEquals(range1, 15)

        val t2= dummyTimetables.copy(timetable = listOf(dummyTimetableLecture.copy(classTime = listOf(29)))) // given

        val range2 = t2.formatTimeRange()

        assertEquals(range2, 15)
    }
}
