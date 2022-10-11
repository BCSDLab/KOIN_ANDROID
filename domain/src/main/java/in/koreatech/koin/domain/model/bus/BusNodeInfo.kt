package `in`.koreatech.koin.domain.model.bus

import java.time.LocalTime

sealed class BusNodeInfo {
    data class ExpressNodeInfo(
        val departureTime: LocalTime,
        val arrivalTime: LocalTime,
        val charge: Int
    ) : BusNodeInfo()

    data class ShuttleNodeInfo(
        val node: String,
        val arrivalTime: LocalTime
    ): BusNodeInfo()

    data class CitybusNodeInfo(
        val startLocation: String,
        val timeInfo: String
    ): BusNodeInfo()
}
