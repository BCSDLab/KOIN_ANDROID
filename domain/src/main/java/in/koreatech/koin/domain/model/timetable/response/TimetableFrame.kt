package `in`.koreatech.koin.domain.model.timetable.response

data class TimetableFrame(
    val id: Int,
    val timetableName: String = "",
    val isMain: Boolean = false
)
