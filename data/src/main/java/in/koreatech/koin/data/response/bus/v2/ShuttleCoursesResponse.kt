package `in`.koreatech.koin.data.response.bus.v2

import `in`.koreatech.koin.domain.model.bus.v2.ShuttleCourse
import `in`.koreatech.koin.domain.model.bus.v2.ShuttleCourseRoute
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

@Serializable
data class ShuttleCourseResponse(
    @SerialName("region") val region: String?,
    @SerialName("routes") val routes: List<ShuttleCourseRouteResponse>?
) {
    fun toShuttleCourse() = ShuttleCourse(
        region = region.orEmpty(),
        routes = routes?.map { it.toShuttleCourseRoute() }.orEmpty()
    )
}

@Serializable
data class ShuttleCourseRouteResponse(
    @SerialName("id") val id: String?,
    @SerialName("type") val type: String?,
    @SerialName("route_name") val routeName: String?,
    @SerialName("sub_name") val subName: String?,
) {
    fun toShuttleCourseRoute() = ShuttleCourseRoute(
        id = id.orEmpty(),
        type = type.orEmpty(),
        routeName = routeName.orEmpty(),
        subName = subName.orEmpty()
    )
}

@Serializable
data class ShuttleSemesterResponse(
    @SerialName("name") val name: String?,
    @SerialName("term") val term: String?,
) {
    fun toShuttleSemester() = ShuttleSemester(
        name = name.orEmpty(),
        term = term.orEmpty()
    )
}