package `in`.koreatech.bus.state

import androidx.compose.runtime.Immutable
import `in`.koreatech.bus.type.ShuttleBusOperationType
import `in`.koreatech.bus.util.CIRCULATION
import `in`.koreatech.bus.util.WEEKEND
import `in`.koreatech.koin.domain.model.bus.ShuttleTimetable

@Immutable
data class ShuttleTimetableState(
    val region: String,
    val routeType: ShuttleBusOperationType,
    val routeName: String,
    val subTitle: String,
    val nodeInfo: List<ShuttleTimetableNodeInfoState>,
    val routeInfo: List<ShuttleTimetableRouteInfoState>,
) {

    /**
     * 등교 하교 시간표가 모두 있는 경우에만 탭 표시
     */
    fun showTabs(): Boolean {
        return routeType == ShuttleBusOperationType.WEEKDAY && routeInfo.size == 2
    }
}

fun ShuttleTimetable.toShuttleTimetableState() = ShuttleTimetableState(
    region = region,
    routeType = when (routeType) {
        CIRCULATION -> ShuttleBusOperationType.CIRCULATION
        WEEKEND -> ShuttleBusOperationType.WEEKEND
        else -> ShuttleBusOperationType.WEEKDAY
    },
    routeName = routeName,
    subTitle = subTitle,
    nodeInfo = nodeInfo.map { it.toShuttleTimetableNodeInfoState() },
    routeInfo = routeInfo.map { it.toShuttleTimetableRouteInfoState() }
)