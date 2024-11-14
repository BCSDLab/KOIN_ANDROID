package `in`.koreatech.koin.data.request.timetable

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.timetable.response.Lecture

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
    val grades: String?,
    @SerializedName("memo")
    val memo: String?,
)

fun Lecture.toCustomLectureQueryRequest() = LectureQueryRequest(
    classTitle = name,
    classTime = classTime,
    classPlace = place ?: "",
    professor = professor,
    lectureId = null,
    grades = null,
    memo = null
)


fun Lecture.toLectureQueryRequest() = LectureQueryRequest(
    lectureId = id,
    classTitle = name,
    classTime = classTime,
    classPlace = "",
    professor = professor,
    grades = grades,
    memo = ""
)
