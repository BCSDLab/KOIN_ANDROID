package `in`.koreatech.koin.data.response.timetable

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.timetable.response.TimetableFrame

data class TimetableFrameResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("timetable_name")
    val timetableName: String?,
    @SerializedName("is_main")
    val isMain: Boolean,
) {
    fun toTimetableFrameResponse(): TimetableFrame = TimetableFrame(
        id = id,
        timetableName = timetableName.orEmpty(),
        isMain = isMain
    )
}
