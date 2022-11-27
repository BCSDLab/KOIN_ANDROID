package `in`.koreatech.koin.domain.model.bus.timetable

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

    data class CitybusNodeInfo(
        val startLocation: String,
        val timeInfo: String
    ): BusNodeInfo()
}
