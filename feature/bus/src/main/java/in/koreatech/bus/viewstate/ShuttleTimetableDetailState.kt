package `in`.koreatech.bus.viewstate

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.domain.model.bus.v2.ShuttleTimetableDetail

@Immutable
data class ShuttleTimetableDetailState(
    val region: String,
    val routeType: String,
    val routeName: String,
    val subTitle: String,
    val nodeInfo: List<ShuttleTimetableNodeInfoState>,
    val routeInfo: List<ShuttleTimetableRouteInfoState>,
)

fun ShuttleTimetableDetail.toShuttleTimetableDetailState() = ShuttleTimetableDetailState(
    region = region,
    routeType = routeType,
    routeName = routeName,
    subTitle = subTitle,
    nodeInfo = nodeInfo.map { it.toShuttleTimetableNodeInfoState() },
    routeInfo = routeInfo.map { it.toShuttleTimetableRouteInfoState() }
)