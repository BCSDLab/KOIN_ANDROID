package `in`.koreatech.bus.state

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.domain.model.bus.v2.ShuttleTimetable

@Immutable
data class ShuttleTimetableState(
    val region: String,
    val routeType: String,
    val routeName: String,
    val subTitle: String,
    val nodeInfo: List<ShuttleTimetableNodeInfoState>,
    val routeInfo: List<ShuttleTimetableRouteInfoState>,
)

fun ShuttleTimetable.toShuttleTimetableState() = ShuttleTimetableState(
    region = region,
    routeType = routeType,
    routeName = routeName,
    subTitle = subTitle,
    nodeInfo = nodeInfo.map { it.toShuttleTimetableNodeInfoState() },
    routeInfo = routeInfo.map { it.toShuttleTimetableRouteInfoState() }
)