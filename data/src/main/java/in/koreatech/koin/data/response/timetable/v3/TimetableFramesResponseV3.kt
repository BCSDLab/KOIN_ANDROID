package `in`.koreatech.koin.data.response.timetable.v3

import com.google.gson.annotations.SerializedName

data class TimetableFramesResponseV3(
    @SerializedName("year")
    val year: Int,
    @SerializedName("timetable_frames")
    val semesterFrames: List<SemesterFramesResponse>
) {
    data class SemesterFramesResponse(
        @SerializedName("term")
        val term: String,
        @SerializedName("frames")
        val frames: List<TimetableFrameResponseV3>
    )
}
