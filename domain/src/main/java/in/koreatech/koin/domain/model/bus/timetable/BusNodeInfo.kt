package `in`.koreatech.koin.domain.model.bus.timetable

import `in`.koreatech.koin.domain.model.bus.city.CityBusGeneralDestination
import java.time.LocalTime

sealed class BusNodeInfo {
    data class ExpressNodeInfo(
        val departureTime: String,
        val arrivalTime: String,
        val charge: Int
    ) : BusNodeInfo()

    data class ShuttleNodeInfo(
        val node: String,
        val arrivalTime: String
    ): BusNodeInfo()

    data class CityBusNodeInfo(
        val busNumber: Int,
        val departNode: CityBusGeneralDestination,
        val arrivalNode: CityBusGeneralDestination
    ): BusNodeInfo()
}
