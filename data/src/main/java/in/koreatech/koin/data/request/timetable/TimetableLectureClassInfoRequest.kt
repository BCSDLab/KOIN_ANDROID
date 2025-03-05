package `in`.koreatech.koin.data.request.timetable

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLectureClassInfo

data class TimetableLectureClassInfoRequest(
    @SerializedName("class_time")
    val classTime: List<Int>?,
    @SerializedName("class_place")
    val classPlace: String?,
)

fun TimetableLectureClassInfo.toClassInfoRequest() =
    TimetableLectureClassInfoRequest(
        classTime = classTime,
        classPlace = classPlace,
    )
