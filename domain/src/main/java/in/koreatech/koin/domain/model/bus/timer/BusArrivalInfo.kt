package `in`.koreatech.koin.domain.model.bus.timer

import java.time.LocalTime

sealed class BusArrivalInfo {
    data class ShuttleBusArrivalInfo(
        val nowBusRemainTime: Long?,
        val nextBusRemainTime: Long?,
        val nowBusArrivalTime: LocalTime?,
        val nextBusArrivalTime: LocalTime?,
        val criteria: LocalTime
    ) : BusArrivalInfo()

    data class CommutingBusArrivalInfo(
        val nowBusRemainTime: Long?,
        val nextBusRemainTime: Long?,
        val nowBusArrivalTime: LocalTime?,
        val nextBusArrivalTime: LocalTime?,
        val criteria: LocalTime
    ) : BusArrivalInfo()

    data class ExpressBusArrivalInfo(
        val nowBusRemainTime: Long?,
        val nextBusRemainTime: Long?,
        val nowBusArrivalTime: LocalTime?,
        val nextBusArrivalTime: LocalTime?,
        val criteria: LocalTime
    ) : BusArrivalInfo()

    data class CityBusArrivalInfo(
        val nowBusRemainTime: Long?,
        val nextBusRemainTime: Long?,
        val nowBusArrivalTime: LocalTime?,
        val nextBusArrivalTime: LocalTime?,
        val busNumber: Int?,
        val criteria: LocalTime
    ) : BusArrivalInfo()
}