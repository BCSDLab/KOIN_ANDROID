package `in`.koreatech.koin.data.request.timetable

import com.google.gson.annotations.SerializedName

data class TimetableFrameQueryRequest(
    @SerializedName("timetable_name")
    val timetableName: String,
    @SerializedName("is_main")
    val isMain: Boolean,
)

data class TimetableFrameCreateQueryRequest(
    @SerializedName("semester")
    val semester: String,
    @SerializedName("timetable_name")
    val timetableName: Boolean,
)