package `in`.koreatech.koin.domain.model.bus.timer

import `in`.koreatech.koin.domain.model.bus.BusNode
import `in`.koreatech.koin.domain.model.bus.BusType
import java.time.LocalTime

sealed class BusArrivalInfo(
    open val departure: BusNode,
    open val arrival: BusNode,
    open val nowBusRemainTime: Long?,
    open val nextBusRemainTime: Long?,
    open val nowBusArrivalTime: LocalTime?,
    open val nextBusArrivalTime: LocalTime?,
    open val criteria: LocalTime
) {
    class ShuttleBusArrivalInfo(
        departure: BusNode,
        arrival: BusNode,
        nowBusRemainTime: Long?,
        nextBusRemainTime: Long?,
        nowBusArrivalTime: LocalTime?,
        nextBusArrivalTime: LocalTime?,
        criteria: LocalTime
    ) : BusArrivalInfo(
        departure,
        arrival,
        nowBusRemainTime,
        nextBusRemainTime,
        nowBusArrivalTime,
        nextBusArrivalTime,
        criteria
    )

    class CommutingBusArrivalInfo(
        departure: BusNode,
        arrival: BusNode,
        nowBusRemainTime: Long?,
        nextBusRemainTime: Long?,
        nowBusArrivalTime: LocalTime?,
        nextBusArrivalTime: LocalTime?,
        criteria: LocalTime
    ) : BusArrivalInfo(
        departure,
        arrival,
        nowBusRemainTime,
        nextBusRemainTime,
        nowBusArrivalTime,
        nextBusArrivalTime,
        criteria
    )

    class ExpressBusArrivalInfo(
        departure: BusNode,
        arrival: BusNode,
        nowBusRemainTime: Long?,
        nextBusRemainTime: Long?,
        nowBusArrivalTime: LocalTime?,
        nextBusArrivalTime: LocalTime?,
        criteria: LocalTime
    ) : BusArrivalInfo(
        departure,
        arrival,
        nowBusRemainTime,
        nextBusRemainTime,
        nowBusArrivalTime,
        nextBusArrivalTime,
        criteria
    )

    class CityBusArrivalInfo(
        departure: BusNode,
        arrival: BusNode,
        nowBusRemainTime: Long?,
        nextBusRemainTime: Long?,
        nowBusArrivalTime: LocalTime?,
        nextBusArrivalTime: LocalTime?,
        criteria: LocalTime,
        val busNumber: Int
    ) : BusArrivalInfo(
        departure,
        arrival,
        nowBusRemainTime,
        nextBusRemainTime,
        nowBusArrivalTime,
        nextBusArrivalTime,
        criteria
    )
}

val BusArrivalInfo.busType
    get() = when (this) {
        is BusArrivalInfo.CityBusArrivalInfo -> BusType.City
        is BusArrivalInfo.CommutingBusArrivalInfo -> BusType.Commuting
        is BusArrivalInfo.ExpressBusArrivalInfo -> BusType.Express
        is BusArrivalInfo.ShuttleBusArrivalInfo -> BusType.Shuttle
    }