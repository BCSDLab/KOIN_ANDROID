package `in`.koreatech.koin.domain.model.bus.timetable

import `in`.koreatech.koin.domain.model.bus.city.CityBusGeneralDestination
import `in`.koreatech.koin.domain.model.bus.city.CityBusNumber
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
        val busNumber: CityBusNumber,
        val departNode: CityBusGeneralDestination,
        val arrivalNode: CityBusGeneralDestination
    ): BusNodeInfo()
}
