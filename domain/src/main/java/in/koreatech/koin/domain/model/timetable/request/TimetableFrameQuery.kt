package `in`.koreatech.koin.domain.model.timetable.request

data class TimetableFrameQuery(
    val timetableName: String,
    val isMain: Boolean,
)

data class TimetableFrameCreateQuery(
    val semester: String,
    val timetableName: Boolean,
)
