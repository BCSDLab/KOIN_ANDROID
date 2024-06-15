package `in`.koreatech.koin.data.response.timetable

import com.google.gson.annotations.SerializedName

data class TimetablesResponse(
    @SerializedName("semester")
    val semester: String? = "",
    @SerializedName("timetable")
    val timetables: List<TimetablesLectureResponse>? = emptyList(),
    @SerializedName("grades")
    val grades: Int? = 0,
    @SerializedName("total_grades")
    val totalGrades: Int? = 0,
)