package `in`.koreatech.koin.data.request.timetable

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.timetable.response.Lecture
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLecture

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
    val classTitle: String?,
    @SerializedName("class_time")
    val classTime: List<Int>?,
    @SerializedName("class_place")
    val classPlace: String,
    @SerializedName("professor")
    val professor: String?,
    @SerializedName("grades")
    val grades: String,
    @SerializedName("memo")
    val memo: String,
)
fun TimetableLecture.toCustomLectureQueryRequest() = LectureQueryRequest(
    classTitle = classTitle,
    classTime = classTime,
    classPlace = classPlace,
    professor = professor,
    lectureId = null,
    grades = "0",
    memo = ""
)

fun TimetableLecture.toLectureQueryRequest() = LectureQueryRequest(
    classTitle = null,
    classTime = null,
    classPlace = classPlace,
    professor = null,
    lectureId = lectureId,
    grades = "0",
    memo = ""
)

fun Lecture.toCustomLectureQueryRequest() = LectureQueryRequest(
    classTitle = name,
    classTime = classTime,
    classPlace = place ?: "",
    professor = professor,
    lectureId = null,
    grades = "0",
    memo = ""
)


fun Lecture.toLectureQueryRequest() = LectureQueryRequest(
    classTitle = null,
    classTime = null,
    classPlace = place ?: "",
    professor = null,
    lectureId = id,
    grades = "0",
    memo = ""
)
