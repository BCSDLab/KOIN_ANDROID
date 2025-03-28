package `in`.koreatech.koin.data.request.timetable.v3

import com.google.gson.annotations.SerializedName

data class TimetableRegularLectureRequest(
    @SerializedName("timetable_frame_id")
    val timetableFrameId: Int,
    @SerializedName("timetable_lecture")
    val timetableRegularLectureBody: List<TimetableRegularLectureBody>
) {
    data class TimetableRegularLectureBody(
        @SerializedName("id")
        val id: Int,
        @SerializedName("lecture_id")
        val lectureId: Int,
        @SerializedName("class_title")
        val classTitle: String,
        @SerializedName("course_type")
        val courseType: String,
        @SerializedName("general_education_area")
        val generalEducationArea: String?,
        @SerializedName("class_places")
        val classPlaces: List<String>
    )
}
