package `in`.koreatech.koin.domain.model.timetable.request

import `in`.koreatech.koin.domain.model.timetable.response.TimetableLectureClassInfo

data class TimetableLecturesQuery(
    val timetableFrameId: Int,
    val timetableLecture: List<TimetableLectureQuery> = emptyList()
)

data class TimetableLectureQuery(
    val id: Int,
    val lectureId: Int,
    val classTitle: String = "",
    val classInfos: List<TimetableLectureClassInfo> = emptyList(),
    val professor: String = "",
    val grades: String = "",
    val memo: String = ""
)
