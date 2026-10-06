package `in`.koreatech.koin.data.request.timetable.v3

import com.google.gson.annotations.SerializedName

data class TimetableFrameCreateRequestV3(
    @SerializedName("year")
    val year: Int,
    @SerializedName("term")
    val term: String
)

data class TimetableFrameUpdateRequestV3(
    @SerializedName("name")
    val name: String,
    @SerializedName("is_main")
    val isMain: Boolean
)
