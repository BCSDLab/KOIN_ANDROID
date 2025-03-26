package `in`.koreatech.koin.data.response.timetable.v3

import com.google.gson.annotations.SerializedName

data class TimetableLecturesResponseV3(
    @SerializedName("timetable_frame_id")
    val timetableFrameId: Int,
    @SerializedName("timetable")
    val timetable: List<TimetableLectureResponseV3>?,
    @SerializedName("grades")
    val grades: Int?,
    @SerializedName("total_grades")
    val totalGrades: Int?
)
