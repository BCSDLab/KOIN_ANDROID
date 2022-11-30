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
    data class ShuttleBusArrivalInfo(
        override val departure: BusNode,
        override val arrival: BusNode,
        override val nowBusRemainTime: Long?,
        override val nextBusRemainTime: Long?,
        override val nowBusArrivalTime: LocalTime?,
        override val nextBusArrivalTime: LocalTime?,
        override val criteria: LocalTime
    ) : BusArrivalInfo(
        departure,
        arrival,
        nowBusRemainTime,
        nextBusRemainTime,
        nowBusArrivalTime,
        nextBusArrivalTime,
        criteria
    )

    data class CommutingBusArrivalInfo(
        override val departure: BusNode,
        override val arrival: BusNode,
        override val nowBusRemainTime: Long?,
        override val nextBusRemainTime: Long?,
        override val nowBusArrivalTime: LocalTime?,
        override val nextBusArrivalTime: LocalTime?,
        override val criteria: LocalTime
    ) : BusArrivalInfo(
        departure,
        arrival,
        nowBusRemainTime,
        nextBusRemainTime,
        nowBusArrivalTime,
        nextBusArrivalTime,
        criteria
    )

    data class ExpressBusArrivalInfo(
        override val departure: BusNode,
        override val arrival: BusNode,
        override val nowBusRemainTime: Long?,
        override val nextBusRemainTime: Long?,
        override val nowBusArrivalTime: LocalTime?,
        override val nextBusArrivalTime: LocalTime?,
        override val criteria: LocalTime
    ) : BusArrivalInfo(
        departure,
        arrival,
        nowBusRemainTime,
        nextBusRemainTime,
        nowBusArrivalTime,
        nextBusArrivalTime,
        criteria
    )

    data class CityBusArrivalInfo(
        override val departure: BusNode,
        override val arrival: BusNode,
        override val nowBusRemainTime: Long?,
        override val nextBusRemainTime: Long?,
        override val nowBusArrivalTime: LocalTime?,
        override val nextBusArrivalTime: LocalTime?,
        override val criteria: LocalTime,
        val busNumber: Int?
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