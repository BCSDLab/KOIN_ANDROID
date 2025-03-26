package `in`.koreatech.koin.data.response.bus

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.bus.ShuttleTimetable

data class ShuttleTimetableResponse(
    @SerializedName("region") val region: String?,
    @SerializedName("route_type") val routeType: String?,
    @SerializedName("route_name") val routeName: String?,
    @SerializedName("sub_name") val subTitle: String?,
    @SerializedName("node_info") val nodeInfo: List<ShuttleTimetableNodeInfoResponse>?,
    @SerializedName("route_info") val routeInfo: List<ShuttleTimetableRouteInfoResponse>?
) {
    fun toShuttleTimetable() =
        ShuttleTimetable(
            region = region.orEmpty(),
            routeType = routeType.orEmpty(),
            routeName = routeName.orEmpty(),
            subTitle = subTitle.orEmpty(),
            nodeInfo = nodeInfo?.map { it.toShuttleTimetableNodeInfo() }.orEmpty(),
            routeInfo = routeInfo?.map { it.toShuttleTimetableRouteInfo() }.orEmpty()
        )
}
