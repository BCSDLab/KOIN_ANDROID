package `in`.koreatech.koin.domain.model.bus

sealed class BusRoute {
    data class ShuttleBusRoute(
        val routeName: String,
        val arrivalInfo: List<BusNodeInfo.ShuttleNodeInfo>
    )

    data class CommutingBusRoute(
        val routeName: String,
        val arrivalInfo: List<BusNodeInfo.ShuttleNodeInfo>
    )

    data class ExpressBusRoute(
        val arrivalInfo: List<BusNodeInfo.ExpressNodeInfo>
    )
}