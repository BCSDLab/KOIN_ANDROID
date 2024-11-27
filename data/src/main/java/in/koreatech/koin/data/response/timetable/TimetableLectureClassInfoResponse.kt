package `in`.koreatech.koin.data.response.timetable

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLectureClassInfo

data class TimetableLectureClassInfoResponse(
    @SerializedName("class_time")
    val classTime: List<Int>,
    @SerializedName("class_place")
    val classPlace: String?
) {
    fun toTimetableLectureClassInfo() = TimetableLectureClassInfo(
        classTime = classTime,
        classPlace = classPlace ?: ""
    )
}
