package `in`.koreatech.koin.data.response.bus.v2

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.bus.v2.ShuttleCourseRoute
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class ShuttleCourseRouteResponse(
    @SerializedName("id") val id: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("route_name") val routeName: String?,
    @SerializedName("sub_name") val subName: String?,
) {
    fun toShuttleCourseRoute() = ShuttleCourseRoute(
        id = id.orEmpty(),
        type = type.orEmpty(),
        routeName = routeName.orEmpty(),
        subName = subName.orEmpty()
    )
}