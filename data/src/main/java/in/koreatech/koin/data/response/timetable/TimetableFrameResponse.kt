package `in`.koreatech.koin.data.response.timetable

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.timetable.response.TimetableFrame

@Deprecated("use TimetableFrameResponseV3 instead")
data class TimetableFrameResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("timetable_name")
    val timetableName: String?,
    @SerializedName("is_main")
    val isMain: Boolean
) {
    fun toTimetableFrame(): TimetableFrame =
        TimetableFrame(
            id = id,
            timetableName = timetableName.orEmpty(),
            isMain = isMain
        )
}
