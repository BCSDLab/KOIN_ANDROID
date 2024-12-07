package `in`.koreatech.koin.data.response.bus.v2

import `in`.koreatech.koin.domain.model.bus.v2.ShuttleCourseRoute
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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