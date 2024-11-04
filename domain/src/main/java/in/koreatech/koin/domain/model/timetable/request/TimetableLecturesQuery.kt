package `in`.koreatech.koin.domain.model.timetable.request

data class TimetableLecturesQuery(
    val timetableFrameId: Int,
    val timetableLecture: List<TimetableLectureQuery> = emptyList(),
)

data class TimetableLectureQuery(
    val id: Int,
    val lectureId: Int,
    val classTitle: String = "",
    val classTime: List<Int>,
    val classPlace: String = "",
    val professor: String = "",
    val grades: String = "",
    val memo: String = "",
)
