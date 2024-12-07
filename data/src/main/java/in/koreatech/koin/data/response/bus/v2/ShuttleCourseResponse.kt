package `in`.koreatech.koin.data.response.bus.v2

import `in`.koreatech.koin.domain.model.bus.v2.ShuttleCourse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
