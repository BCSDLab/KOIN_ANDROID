package `in`.koreatech.koin.data.request.timetable

import com.google.gson.annotations.SerializedName

data class TimetableLecturesQueryRequest(
    @SerializedName("timetable_frame_id")
    val timetableFrameId: Int,
    @SerializedName("timetable_lecture")
    val timetableLecture: List<TimetableLectureQueryRequest>,
)

data class TimetableLectureQueryRequest(
    @SerializedName("id")
    val id: Int,
    @SerializedName("lecture_id")
    val lectureId: Int,
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
