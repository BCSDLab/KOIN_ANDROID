package `in`.koreatech.koin.data.request.timetable

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.timetable.request.LectureQuery
import `in`.koreatech.koin.domain.model.timetable.request.LecturesQuery
import `in`.koreatech.koin.domain.model.timetable.request.TimetableLectureQuery
import `in`.koreatech.koin.domain.model.timetable.request.TimetableLecturesQuery

data class LecturesQueryRequest(
    @SerializedName("timetable_frame_id")
    val timetableFrameId: Int,
    @SerializedName("timetable_lecture")
    val timetableLecture: List<LectureQueryRequest>,
)

data class LectureQueryRequest(
    @SerializedName("lecture_id")
    val lectureId: Int?,
    @SerializedName("class_title")
    val classTitle: String,
    @SerializedName("class_time")
    val classTime: List<Int>,
    @SerializedName("class_place")
    val classPlace: String,
    @SerializedName("professor")
    val professor: String,
    @SerializedName("grades")
    val grades: String,
    @SerializedName("memo")
    val memo: String,
)

fun LecturesQuery.toLecturesQueryRequest() = LecturesQueryRequest(
    timetableFrameId = timetableFrameId,
    timetableLecture = timetableLecture.map { it.toLectureQueryRequest() }
)

fun LectureQuery.toLectureQueryRequest() = LectureQueryRequest(
    lectureId = lectureId,
    classTitle = classTitle,
    classTime = classTime,
    classPlace = classPlace,
    professor = professor,
    grades = grades,
    memo = memo
)
