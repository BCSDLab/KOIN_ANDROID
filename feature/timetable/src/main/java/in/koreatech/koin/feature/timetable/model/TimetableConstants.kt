package `in`.koreatech.koin.feature.timetable.model

import androidx.compose.ui.graphics.Color
import `in`.koreatech.koin.domain.model.timetable.response.Lecture
import `in`.koreatech.koin.domain.model.timetable.response.Semester
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLecture
import java.time.DayOfWeek
import java.time.LocalTime

object TimetableConstants {
    val days = listOf("월", "화", "수", "목", "금")
    const val eventHeight = 64
}

val dummyEvent = TimetableEvent(
    id = 1,
    lectureId = 1,
    name = "강의 제목",
    color = TimetableColor(Color(0xFF890000),Color(0x33890000)),
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
    TimetableColor(Color(0xFF890000),Color(0x33890000)),
    TimetableColor(Color(0xFFFF4444),Color(0x33FF4444)),
    TimetableColor(Color(0xFFFF993B),Color(0x33FF993B)),
    TimetableColor(Color(0xFFE8D52A),Color(0x33E8D52A)),
    TimetableColor(Color(0xFFD0AE00),Color(0x33D0AE00)),
    TimetableColor(Color(0xFF513A00),Color(0x33513A00)),
    TimetableColor(Color(0xFF0C9D61),Color(0x330C9D61)),
    TimetableColor(Color(0xFF7ABA78),Color(0x337ABA78)),
    TimetableColor(Color(0xFF366718),Color(0x33366718)),
    TimetableColor(Color(0xFF80C4E9),Color(0x3380C4E9)),
    TimetableColor(Color(0xFF1679AB),Color(0x331679AB)),
    TimetableColor(Color(0xFF074173),Color(0x33074173)),
    TimetableColor(Color(0xFF523AE2),Color(0x33523AE2)),
    TimetableColor(Color(0xFF6F6F6F),Color(0x336F6F6F)),
    TimetableColor(Color(0xFFCBCBCB),Color(0x33CBCBCB)),
)