package `in`.koreatech.koin.data.response.timetable

import com.google.gson.annotations.SerializedName

@Deprecated("use TimetableFramesResponseV3 instead")
data class TimetableFramesResponse(
    @SerializedName("semesters")
    val semesterFrames: Map<String, List<TimetableFrameResponse>>
)
