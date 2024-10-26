package `in`.koreatech.koin.data.response.timetable

import com.google.gson.annotations.SerializedName

data class TimetableLecturesResponse(
    @SerializedName("timetable_frame_id")
    val timetableFrameId: Int,
    @SerializedName("timetable")
    val timetable: List<TimetableLectureResponse>,
    @SerializedName("grades")
    val grades: Int?,
    @SerializedName("total_grades")
    val totalGrades: Int?,
)

