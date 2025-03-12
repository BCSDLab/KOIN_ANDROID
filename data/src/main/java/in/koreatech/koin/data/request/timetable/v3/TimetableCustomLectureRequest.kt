package `in`.koreatech.koin.data.request.timetable.v3

import com.google.gson.annotations.SerializedName

data class TimetableCustomLectureRequest(
    @SerializedName("timetable_frame_id")
    val timetableFrameId: Int,
    @SerializedName("timetable_lecture")
    val timetableCustomLectureBody: TimetableCustomLectureBody,
) {
    data class TimetableCustomLectureBody(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("class_title")
        val classTitle: String?,
        @SerializedName("lecture_infos")
        val lectureInfos: List<TimetableCustomLectureInfo>,
        @SerializedName("professor")
        val professor: String?,
    )

    data class TimetableCustomLectureInfo(
        @SerializedName("start_time")
        val startTime: Int,
        @SerializedName("end_time")
        val endTime: Int,
        @SerializedName("place")
        val place: String?,
    )
}
