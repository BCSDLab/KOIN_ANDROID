package `in`.koreatech.koin.domain.model.timetable.response

data class Lecture(
    val id: Int,
    val code: String = "",
    val name: String = "",
    val grades: String = "",
    val lectureClass: String = "",
    val regularNumber: String = "",
    val department: String = "",
    val target: String = "",
    val professor: String = "",
    val isEnglish: String = "",
    val designScore: String = "",
    val isElearning: String = "",
    val classTime: List<Int>,
)