package `in`.koreatech.koin.data.response.timetable.v3

import com.google.gson.annotations.SerializedName

data class TimetableLectureInfoResponse(
    @SerializedName("day")
    val day: Int,
    @SerializedName("start_time")
    val startTime: Int,
    @SerializedName("end_time")
    val endTime: Int,
    @SerializedName("place")
    val place: String?,
)
