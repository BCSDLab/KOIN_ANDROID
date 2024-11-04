package `in`.koreatech.koin.domain.model.timetable.request

data class LecturesQuery(
    val timetableFrameId: Int,
    val timetableLecture: List<LectureQuery> = emptyList(),
)

data class LectureQuery(
    val lectureId: Int?,
    val classTitle: String = "",
    val classTime: List<Int>,
    val classPlace: String = "",
    val professor: String = "",
    val grades: String = "",
    val memo: String = "",
)