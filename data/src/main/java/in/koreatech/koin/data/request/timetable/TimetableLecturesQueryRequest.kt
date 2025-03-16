package `in`.koreatech.koin.data.request.timetable

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.timetable.request.TimetableLectureQuery
import `in`.koreatech.koin.domain.model.timetable.request.TimetableLecturesQuery

data class TimetableLecturesQueryRequest(
    @SerializedName("timetable_frame_id")
    val timetableFrameId: Int,
    @SerializedName("timetable_lecture")
    val timetableLecture: List<TimetableLectureQueryRequest>
)

data class TimetableLectureQueryRequest(
    @SerializedName("id")
    val id: Int,
    @SerializedName("lecture_id")
    val lectureId: Int,
    @SerializedName("class_title")
    val classTitle: String,
    @SerializedName("class_infos")
    val classInfos: List<TimetableLectureClassInfoRequest>,
    @SerializedName("professor")
    val professor: String,
    @SerializedName("grades")
    val grades: String,
    @SerializedName("memo")
    val memo: String
)

fun TimetableLecturesQuery.toTimetableLecturesQueryRequest() =
    TimetableLecturesQueryRequest(
        timetableFrameId = timetableFrameId,
        timetableLecture = timetableLecture.map { it.toTimetableLectureQueryRequest() }
    )

fun TimetableLectureQuery.toTimetableLectureQueryRequest() =
    TimetableLectureQueryRequest(
        id = id,
        lectureId = lectureId,
        classTitle = classTitle,
        classInfos = classInfos.map { it.toClassInfoRequest() },
        professor = professor,
        grades = grades,
        memo = memo
    )
