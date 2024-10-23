package `in`.koreatech.koin.domain.model.timetable.response

data class TimetableLectures(
    val timetableFrameId: Int,
    val timetable: List<TimetableLecture>,
    val grades: Int,
    val totalGrades: Int,
)
