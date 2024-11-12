package `in`.koreatech.koin.feature.timetable

import `in`.koreatech.koin.domain.model.timetable.response.Lecture
import `in`.koreatech.koin.domain.model.timetable.response.Semester

object SampleTimetable {
    val semester = Semester(id = 1, semester = "20242")

    val lecture = Lecture(
        id = 1, code = "HRD011", name = "직업능력개발훈련평가",
        professor = "우성민", grades = "2", lectureClass = "01", regularNumber = "40",
        department = "HRD학과", target = "전기3", isEnglish = "", isElearning = "",
        designScore = "0", classTime = listOf(310, 311, 312, 313)
    )
}