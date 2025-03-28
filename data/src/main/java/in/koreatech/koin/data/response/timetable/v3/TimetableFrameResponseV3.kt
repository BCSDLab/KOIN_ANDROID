package `in`.koreatech.koin.data.response.timetable.v3

import com.google.gson.annotations.SerializedName

data class TimetableFrameResponseV3(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("is_main")
    val isMain: Boolean
)
