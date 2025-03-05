package `in`.koreatech.koin.feature.timetable

import `in`.koreatech.koin.domain.model.timetable.response.TimetableLecture
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLectureClassInfo
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLectures
import org.junit.Assert.assertEquals
import org.junit.Test

class TimetableLecturesTest {
    val dummyTimetableLecture =
        TimetableLecture(
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
    val dummyTimetables =
        TimetableLectures(
            timetableFrameId = 1,
            timetable = listOf(dummyTimetableLecture),
            grades = 1,
            totalGrades = 1,
        )

    @Test
    fun `강의 시간이 없을 때, 시간 범위는 9(9시~18시) 반환된다`() {
        val timetables =
            dummyTimetables.copy(
                timetable =
                    listOf(
                        dummyTimetableLecture.copy(classInfos = emptyList()),
                    ),
            ) // given

        val range = timetables.formatTimeRange()

        assertEquals(range, 9)
    }

    @Test
    fun `강의 시간이 0에서 17사이 일 때, 시간 범위는 9(9시~18시) 반환된다`() {
        val timetables =
            dummyTimetables.copy(
                timetable =
                    listOf(
                        dummyTimetableLecture.copy(
                            classInfos =
                                listOf(
                                    TimetableLectureClassInfo(
                                        classTime = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17),
                                        classPlace = "",
                                    ),
                                ),
                        ),
                    ),
            ) // given

        val range = timetables.formatTimeRange()

        assertEquals(range, 9)
    }

    @Test
    fun `강의 시간이 18,19일 때, 시간 범위는 10(9시~19시) 반환된다`() {
        val t1 =
            dummyTimetables.copy(
                timetable =
                    listOf(
                        dummyTimetableLecture.copy(
                            classInfos =
                                listOf(
                                    TimetableLectureClassInfo(
                                        classTime = listOf(18),
                                        classPlace = "",
                                    ),
                                ),
                        ),
                    ),
            )

        val range1 = t1.formatTimeRange()

        assertEquals(range1, 10)

        val t2 =
            dummyTimetables.copy(
                timetable =
                    listOf(
                        dummyTimetableLecture.copy(
                            classInfos =
                                listOf(
                                    TimetableLectureClassInfo(
                                        classTime = listOf(19),
                                        classPlace = "",
                                    ),
                                ),
                        ),
                    ),
            )

        val range2 = t2.formatTimeRange()

        assertEquals(range2, 10)
    }

    @Test
    fun `강의 시간이 20,21일 때, 시간 범위는 11(9시~20시) 반환된다`() {
        val t1 =
            dummyTimetables.copy(
                timetable =
                    listOf(
                        dummyTimetableLecture.copy(
                            classInfos =
                                listOf(
                                    TimetableLectureClassInfo(
                                        classTime = listOf(20),
                                        classPlace = "",
                                    ),
                                ),
                        ),
                    ),
            )

        val range1 = t1.formatTimeRange()

        assertEquals(range1, 11)

        val t2 =
            dummyTimetables.copy(
                timetable =
                    listOf(
                        dummyTimetableLecture.copy(
                            classInfos =
                                listOf(
                                    TimetableLectureClassInfo(
                                        classTime = listOf(21),
                                        classPlace = "",
                                    ),
                                ),
                        ),
                    ),
            )

        val range2 = t2.formatTimeRange()

        assertEquals(range2, 11)
    }

    @Test
    fun `강의 시간이 22,23일 때, 시간 범위는 12(9시~21시) 반환된다`() {
        val t1 =
            dummyTimetables.copy(
                timetable =
                    listOf(
                        dummyTimetableLecture.copy(
                            classInfos =
                                listOf(
                                    TimetableLectureClassInfo(
                                        classTime = listOf(22),
                                        classPlace = "",
                                    ),
                                ),
                        ),
                    ),
            )

        val range1 = t1.formatTimeRange()

        assertEquals(range1, 12)

        val t2 =
            dummyTimetables.copy(
                timetable =
                    listOf(
                        dummyTimetableLecture.copy(
                            classInfos =
                                listOf(
                                    TimetableLectureClassInfo(
                                        classTime = listOf(23),
                                        classPlace = "",
                                    ),
                                ),
                        ),
                    ),
            )

        val range2 = t2.formatTimeRange()

        assertEquals(range2, 12)
    }

    @Test
    fun `강의 시간이 24,25일 때, 시간 범위는 13(9시~22시) 반환된다`() {
        val t1 =
            dummyTimetables.copy(
                timetable =
                    listOf(
                        dummyTimetableLecture.copy(
                            classInfos =
                                listOf(
                                    TimetableLectureClassInfo(
                                        classTime = listOf(24),
                                        classPlace = "",
                                    ),
                                ),
                        ),
                    ),
            )

        val range1 = t1.formatTimeRange()

        assertEquals(range1, 13)

        val t2 =
            dummyTimetables.copy(
                timetable =
                    listOf(
                        dummyTimetableLecture.copy(
                            classInfos =
                                listOf(
                                    TimetableLectureClassInfo(
                                        classTime = listOf(25),
                                        classPlace = "",
                                    ),
                                ),
                        ),
                    ),
            )

        val range2 = t2.formatTimeRange()

        assertEquals(range2, 13)
    }

    @Test
    fun `강의 시간이 26,27 때, 시간 범위는 14(9시~23시)로 반환된다`() {
        val t1 =
            dummyTimetables.copy(
                timetable =
                    listOf(
                        dummyTimetableLecture.copy(
                            classInfos =
                                listOf(
                                    TimetableLectureClassInfo(
                                        classTime = listOf(26),
                                        classPlace = "",
                                    ),
                                ),
                        ),
                    ),
            )

        val range1 = t1.formatTimeRange()

        assertEquals(range1, 14)

        val t2 =
            dummyTimetables.copy(
                timetable =
                    listOf(
                        dummyTimetableLecture.copy(
                            classInfos =
                                listOf(
                                    TimetableLectureClassInfo(
                                        classTime = listOf(27),
                                        classPlace = "",
                                    ),
                                ),
                        ),
                    ),
            )

        val range2 = t2.formatTimeRange()

        assertEquals(range2, 14)
    }

    @Test
    fun `강의 시간이 28,29일 때, 시간 범위는 15(9시~24시)로 반환된다`() {
        val t1 =
            dummyTimetables.copy(
                timetable =
                    listOf(
                        dummyTimetableLecture.copy(
                            classInfos =
                                listOf(
                                    TimetableLectureClassInfo(
                                        classTime = listOf(28),
                                        classPlace = "",
                                    ),
                                ),
                        ),
                    ),
            )

        val range1 = t1.formatTimeRange()

        assertEquals(range1, 15)

        val t2 =
            dummyTimetables.copy(
                timetable =
                    listOf(
                        dummyTimetableLecture.copy(
                            classInfos =
                                listOf(
                                    TimetableLectureClassInfo(
                                        classTime = listOf(29),
                                        classPlace = "",
                                    ),
                                ),
                        ),
                    ),
            )
        val range2 = t2.formatTimeRange()

        assertEquals(range2, 15)
    }
}
