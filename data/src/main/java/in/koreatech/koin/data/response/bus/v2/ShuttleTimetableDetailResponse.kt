package `in`.koreatech.koin.data.response.bus.v2

import `in`.koreatech.koin.domain.model.bus.v2.ShuttleTimetableDetail
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShuttleTimetableDetailResponse(
    @SerialName("region") val region: String?,
    @SerialName("route_type") val routeType: String?,
    @SerialName("route_name") val routeName: String?,
    @SerialName("sub_name") val subTitle: String?,
    @SerialName("node_info") val nodeInfo: List<ShuttleTimetableNodeInfoResponse>?,
    @SerialName("route_info") val routeInfo: List<ShuttleTimetableRouteInfoResponse>?,
) {
    fun toShuttleTimetableDetail() = ShuttleTimetableDetail(
        region = region.orEmpty(),
        routeType = routeType.orEmpty(),
        routeName = routeName.orEmpty(),
        subTitle = subTitle.orEmpty(),
        nodeInfo = nodeInfo?.map { it.toShuttleTimetableNodeInfo() }.orEmpty(),
        routeInfo = routeInfo?.map { it.toShuttleTimetableRouteInfo() }.orEmpty()
    )
}
