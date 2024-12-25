package `in`.koreatech.koin.data.response.bus.v2

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.bus.v2.ShuttleTimetable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class ShuttleTimetableResponse(
    @SerializedName("region") val region: String?,
    @SerializedName("route_type") val routeType: String?,
    @SerializedName("route_name") val routeName: String?,
    @SerializedName("sub_name") val subTitle: String?,
    @SerializedName("node_info") val nodeInfo: List<ShuttleTimetableNodeInfoResponse>?,
    @SerializedName("route_info") val routeInfo: List<ShuttleTimetableRouteInfoResponse>?,
) {
    fun toShuttleTimetable() = ShuttleTimetable(
        region = region.orEmpty(),
        routeType = routeType.orEmpty(),
        routeName = routeName.orEmpty(),
        subTitle = subTitle.orEmpty(),
        nodeInfo = nodeInfo?.map { it.toShuttleTimetableNodeInfo() }.orEmpty(),
        routeInfo = routeInfo?.map { it.toShuttleTimetableRouteInfo() }.orEmpty()
    )
}
