package `in`.koreatech.koin.data.response.timetable

import com.google.gson.annotations.SerializedName

data class SemesterResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("semester")
    val semester: String,
)