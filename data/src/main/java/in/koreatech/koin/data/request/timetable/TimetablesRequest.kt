package `in`.koreatech.koin.data.request.timetable

import com.google.gson.annotations.SerializedName

data class TimetablesRequest(
    @SerializedName("timetable")
    val timetable: List<TimetablesLectureRequest>,
    @SerializedName("semester")
    val semester: String,
)