package `in`.koreatech.koin.domain.model.bus.timetable

import `in`.koreatech.koin.domain.model.bus.BusDirection
import `in`.koreatech.koin.domain.model.bus.timetable.BusNodeInfo

sealed class BusRoute {
    data class ShuttleBusRoute(
        val routeName: String,
        val arrivalInfo: List<BusNodeInfo.ShuttleNodeInfo>
    ): BusRoute()

    data class ExpressBusRoute(
        val arrivalInfo: List<BusNodeInfo.ExpressNodeInfo>
    ): BusRoute()

    data class CityBusRoute(
        val arrivalInfo: List<BusNodeInfo.CitybusNodeInfo>
    ): BusRoute()
}