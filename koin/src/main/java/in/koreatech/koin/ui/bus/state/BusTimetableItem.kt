package `in`.koreatech.koin.ui.bus.state

import java.time.LocalTime

sealed class BusTimetableItem {

    data class ExpressArrivalInfo(
        val departureTime: LocalTime,
        val arrivalTime: LocalTime
    ) : BusTimetableItem()

    data class ShuttleArrivalInfo(
        val node: String,
        val arrivalTime: LocalTime
    ): BusTimetableItem()

    data class CitybusArrivalInfo(
        val startLocation: String,
        val timeInfo: String
    ): BusTimetableItem()
}
