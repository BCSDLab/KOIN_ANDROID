package `in`.koreatech.koin.domain.model.bus

data class ShuttleTimetable(
    val region: String,
    val routeType: String,
    val routeName: String,
    val subTitle: String,
    val nodeInfo: List<ShuttleTimetableNodeInfo>,
    val routeInfo: List<ShuttleTimetableRouteInfo>,
)
