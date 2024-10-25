package `in`.koreatech.koin.data.response.timetable

import com.google.gson.annotations.SerializedName

data class TimetableFrameResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("timetable_name")
    val timetableName: String?,
    @SerializedName("is_main")
    val isMain: Boolean,
)
