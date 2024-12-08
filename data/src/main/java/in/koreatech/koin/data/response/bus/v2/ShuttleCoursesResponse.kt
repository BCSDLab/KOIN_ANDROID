package `in`.koreatech.koin.data.response.bus.v2

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.bus.v2.ShuttleCourses
import `in`.koreatech.koin.domain.model.bus.v2.ShuttleSemester
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class ShuttleCoursesResponse(
    @SerializedName("route_categories") val courses: List<ShuttleCourseResponse>?,
    @SerializedName("semester") val semester: ShuttleSemesterResponse?,
) {
    fun toShuttleCourses() = ShuttleCourses(
        courses = courses?.map { it.toShuttleCourse() }.orEmpty(),
        semester = semester?.toShuttleSemester() ?: ShuttleSemester("", "")
    )
}
