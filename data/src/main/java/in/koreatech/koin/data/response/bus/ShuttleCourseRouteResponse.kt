package `in`.koreatech.koin.data.response.bus

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.bus.ShuttleCourseRoute

data class ShuttleCourseRouteResponse(
    @SerializedName("id") val id: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("route_name") val routeName: String?,
    @SerializedName("sub_name") val subName: String?,
) {
    fun toShuttleCourseRoute() =
        ShuttleCourseRoute(
            id = id.orEmpty(),
            type = type.orEmpty(),
            routeName = routeName.orEmpty(),
            subName = subName.orEmpty(),
        )
}
