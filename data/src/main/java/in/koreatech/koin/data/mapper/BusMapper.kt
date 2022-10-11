package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.constant.BUS_RESPONSE_TIME_FORMAT
import `in`.koreatech.koin.data.response.bus.*
import `in`.koreatech.koin.domain.model.bus.*
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun BusCourseResponse.toBusCourse() = BusCourse(
    busType = busType.toBusType(),
    direction = direction.toBusDirection(),
    region = region
)

fun BusTimetableResponse.toCommutingBusRoute(): BusRoute.CommutingBusRoute {
    return BusRoute.CommutingBusRoute(
        routeName = routeName,
        arrivalInfo = arrivalInfo.map { route ->
            BusNodeInfo.ShuttleNodeInfo(
                node = route.nodeName,
                arrivalTime = LocalTime.parse(
                    route.arrivalTime, DateTimeFormatter.ofPattern(
                        BUS_RESPONSE_TIME_FORMAT
                    )
                )
            )
        }
    )
}

fun BusTimetableResponse.toShuttleBusRoute(): BusRoute.ShuttleBusRoute {
    return BusRoute.ShuttleBusRoute(
        routeName = routeName,
        arrivalInfo = arrivalInfo.map { route ->
            BusNodeInfo.ShuttleNodeInfo(
                node = route.nodeName,
                arrivalTime = LocalTime.parse(
                    route.arrivalTime, DateTimeFormatter.ofPattern(
                        BUS_RESPONSE_TIME_FORMAT
                    )
                )
            )
        }
    )
}

fun List<ExpressBusRouteResponse>.toExpressBusRoute(): BusRoute.ExpressBusRoute {
    return BusRoute.ExpressBusRoute(
        arrivalInfo = map { route ->
            BusNodeInfo.ExpressNodeInfo(
                departureTime =  LocalTime.parse(
                    route.departure, DateTimeFormatter.ofPattern(
                        BUS_RESPONSE_TIME_FORMAT
                    )
                ),
                arrivalTime =  LocalTime.parse(
                    route.arrival, DateTimeFormatter.ofPattern(
                        BUS_RESPONSE_TIME_FORMAT
                    )
                ),
                charge = route.charge
            )
        }
    )
}

fun BusResponse.toShuttleBusRemainTimePair() =
    nowBus?.let {
        BusRemainTime.ShuttleBusRemainTime(it.remainTimeSecond)
    } to nextBus?.let {
        BusRemainTime.ShuttleBusRemainTime(it.remainTimeSecond)
    }

fun BusResponse.toCommutingBusRemainTimePair() =
    nowBus?.let {
        BusRemainTime.CommutingBusRemainTime(it.remainTimeSecond)
    } to nextBus?.let {
        BusRemainTime.CommutingBusRemainTime(it.remainTimeSecond)
    }

fun BusResponse.toExpressBusRemainTimePair() =
    nowBus?.let {
        BusRemainTime.ExpressBusRemainTime(it.remainTimeSecond)
    } to nextBus?.let {
        BusRemainTime.ExpressBusRemainTime(it.remainTimeSecond)
    }

fun BusResponse.toCityBusRemainTimePair() =
    nowBus?.let {
        BusRemainTime.CityBusRemainTime(
            it.busNumber ?: -1, it.remainTimeSecond
        )
    } to nextBus?.let {
        BusRemainTime.CityBusRemainTime(
            it.busNumber?: -1, it.remainTimeSecond)
    }

fun BusSearchResponse.toBusSearchResult(searchDateTime: LocalDateTime) : BusSearchResult {
    val searchTime = LocalTime.parse(
        busTime, DateTimeFormatter.ofPattern(
            BUS_RESPONSE_TIME_FORMAT
        )
    )

    return BusSearchResult(
        busType = busName.toBusType(),
        busTime = LocalDateTime.of(searchDateTime.toLocalDate(), searchTime)
    )
}