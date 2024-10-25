package `in`.koreatech.koin.domain.model.timetable.response

data class TimetableLecture(
    val id: Int,
    val lectureId: Int,
    val regularNumber: String = "",
    val code: String = "",
    val designScore: String = "",
    val classTime: List<Int>,
    val classPlace: String = "",
    val memo: String = "",
    val grades: String = "",
    val classTitle: String = "",
    val lectureClass: String = "",
    val target: String = "",
    val professor: String = "",
    val department: String = "",
)
