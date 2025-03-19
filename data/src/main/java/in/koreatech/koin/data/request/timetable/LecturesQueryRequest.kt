package `in`.koreatech.koin.data.request.timetable

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.timetable.response.Lecture
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLecture

data class LecturesQueryRequest(
    @SerializedName("timetable_frame_id")
    val timetableFrameId: Int,
    @SerializedName("timetable_lecture")
    val timetableLecture: List<LectureQueryRequest>
)

data class LectureQueryRequest(
    @SerializedName("lecture_id")
    val lectureId: Int?,
    @SerializedName("class_title")
    val classTitle: String?,
    @SerializedName("class_infos")
    val classInfos: List<TimetableLectureClassInfoRequest>?,
    @SerializedName("professor")
    val professor: String?,
    @SerializedName("grades")
    val grades: String,
    @SerializedName("memo")
    val memo: String
)

fun TimetableLecture.toCustomLectureQueryRequest() =
    LectureQueryRequest(
        classTitle = classTitle,
        classInfos = classInfos.map { it.toClassInfoRequest() },
        professor = professor,
        lectureId = null,
        grades = "0",
        memo = ""
    )

fun TimetableLecture.toLectureQueryRequest() =
    LectureQueryRequest(
        classTitle = null,
        classInfos = null,
        professor = null,
        lectureId = lectureId,
        grades = "0",
        memo = ""
    )

fun Lecture.toCustomLectureQueryRequest(classInfos: List<TimetableLectureClassInfoRequest>) =
    LectureQueryRequest(
        classTitle = name,
        classInfos = classInfos,
        professor = professor,
        lectureId = null,
        grades = "0",
        memo = ""
    )

fun Lecture.toLectureQueryRequest() =
    LectureQueryRequest(
        classTitle = null,
        classInfos = null,
        professor = null,
        lectureId = id,
        grades = "0",
        memo = ""
    )
