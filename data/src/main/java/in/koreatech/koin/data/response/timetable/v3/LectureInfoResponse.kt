package `in`.koreatech.koin.data.response.timetable.v3

import com.google.gson.annotations.SerializedName

/**
 * @param day 요일
 * @param startTime 시작 시간
 * @param endTime 끝나는 시간
 */
data class LectureInfoResponse(
    @SerializedName("day")
    val day: Int,
    @SerializedName("start_time")
    val startTime: Int,
    @SerializedName("end_time")
    val endTime: Int
)
