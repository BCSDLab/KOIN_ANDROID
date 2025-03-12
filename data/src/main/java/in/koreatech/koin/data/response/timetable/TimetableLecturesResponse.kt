package `in`.koreatech.koin.data.response.timetable

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLectures

@Deprecated("use TimetableLecturesResponseV3 instead")
data class TimetableLecturesResponse(
    @SerializedName("timetable_frame_id")
    val timetableFrameId: Int,
    @SerializedName("timetable")
    val timetable: List<TimetableLectureResponse>,
    @SerializedName("grades")
    val grades: Int?,
    @SerializedName("total_grades")
    val totalGrades: Int?,
) {
    fun toTimetableLectures() =
        TimetableLectures(
            timetableFrameId = timetableFrameId,
            timetable = timetable.map { it.toTimetableLecture() },
            grades = grades ?: 0,
            totalGrades = totalGrades ?: 0,
        )
}
