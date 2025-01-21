package `in`.koreatech.koin.data.response.timetable

import com.google.gson.annotations.SerializedName

data class TimetableFramesResponse(
    @SerializedName("semesters")
    val semesterFrames: Map<String, List<TimetableFrameResponse>>
)