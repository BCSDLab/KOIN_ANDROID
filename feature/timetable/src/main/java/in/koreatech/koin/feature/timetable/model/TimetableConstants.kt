package `in`.koreatech.koin.feature.timetable.model

import androidx.compose.ui.graphics.Color
import `in`.koreatech.koin.domain.model.timetable.response.Lecture
import java.time.DayOfWeek
import java.time.LocalTime

object TimetableConstants {
    val days = listOf("월", "화", "수", "목", "금")
    const val eventHeight = 64
}

val dummyEvent = TimetableEvent(
    id = 1,
    name = "강의 제목",
    color = Color(0xFFAFBBF2),
    dayOfWeek = DayOfWeek.FRIDAY,
    start = LocalTime.of(16, 0),
    end = LocalTime.of(18, 0),
    description = "설명",
)

val dummyLecture = Lecture(
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
    classTime = listOf(
        310,
        311,
        312,
        313
    )
)

val defaultColors = listOf(
    Color(0xfffdbcf5),
    Color(0xfffdbcf5),
    Color(0xfffedb8f),
    Color(0xffc2eead),
    Color(0xffffb588),
    Color(0xffffa9b7),
    Color(0xff8ae9ff),
    Color(0xff60e4c1),
    Color(0xffb4bfff),
    Color(0xff72b0ff),
    Color(0xffe0e5eb)
)