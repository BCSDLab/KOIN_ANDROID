package `in`.koreatech.koin.data.response.bus.v2

import `in`.koreatech.koin.domain.model.bus.v2.ShuttleCourses
import `in`.koreatech.koin.domain.model.bus.v2.ShuttleSemester
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShuttleCoursesResponse(
    @SerialName("route_categories") val courses: List<ShuttleCourseResponse>?,
    @SerialName("semester") val semester: ShuttleSemesterResponse?,
) {
    fun toShuttleCourses() = ShuttleCourses(
        courses = courses?.map { it.toShuttleCourse() }.orEmpty(),
        semester = semester?.toShuttleSemester() ?: ShuttleSemester("", "")
    )
}
