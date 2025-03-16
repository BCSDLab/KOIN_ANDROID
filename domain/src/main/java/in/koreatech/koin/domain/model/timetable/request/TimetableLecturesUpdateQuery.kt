package `in`.koreatech.koin.domain.model.timetable.request

data class TimetableLecturesUpdateQueryRequest(
    val timetableFrameId: Int,
    val timetableLecture: List<TimetableLectureUpdateQueryRequest>
)

data class TimetableLectureUpdateQueryRequest(
    val lectureId: Int,
    val classTitle: String,
    val classTime: List<Int>,
    val classPlace: String,
    val professor: String,
    val grades: String,
    val memo: String
)
