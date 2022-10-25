package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.constant.BUS_RESPONSE_TIME_FORMAT
import `in`.koreatech.koin.data.response.bus.*
import `in`.koreatech.koin.data.util.nowTime
import `in`.koreatech.koin.domain.model.bus.*
import android.util.Log
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
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

fun BusResponse.toShuttleBusArrivalInfo() = nowTime.let { time ->
    BusArrivalInfo.ShuttleBusArrivalInfo(
        nowBusRemainTime = nowBus?.remainTimeSecond,
        nextBusRemainTime = nextBus?.remainTimeSecond,
        nowBusArrivalTime = nowBus?.let {
            time.plusSeconds(it.remainTimeSecond).plusMinutes(1)
        },
        nextBusArrivalTime = nextBus?.let {
            time.plusSeconds(it.remainTimeSecond).plusMinutes(1)
        },
        criteria = time
    )
}


fun BusResponse.toCommutingBusRemainTimePair() =  nowTime.let { time ->
    BusArrivalInfo.CommutingBusArrivalInfo(
        nowBusRemainTime = nowBus?.remainTimeSecond,
        nextBusRemainTime = nextBus?.remainTimeSecond,
        nowBusArrivalTime = nowBus?.let {
            time.plusSeconds(it.remainTimeSecond).plusMinutes(1)
        },
        nextBusArrivalTime = nextBus?.let {
            time.plusSeconds(it.remainTimeSecond).plusMinutes(1)
        },
        criteria = time
    )
}

fun BusResponse.toExpressBusRemainTimePair() = nowTime.let { time ->
    BusArrivalInfo.ExpressBusArrivalInfo(
        nowBusRemainTime = nowBus?.remainTimeSecond,
        nextBusRemainTime = nextBus?.remainTimeSecond,
        nowBusArrivalTime = nowBus?.let {
            time.plusSeconds(it.remainTimeSecond).plusMinutes(1)
        },
        nextBusArrivalTime = nextBus?.let {
            time.plusSeconds(it.remainTimeSecond).plusMinutes(1)
        },
        criteria = time
    )
}

fun BusResponse.toCityBusRemainTimePair() = nowTime.let { time ->
    BusArrivalInfo.CityBusArrivalInfo(
        nowBusRemainTime = nowBus?.remainTimeSecond,
        nextBusRemainTime = nextBus?.remainTimeSecond,
        nowBusArrivalTime = nowBus?.let {
            time.plusSeconds(it.remainTimeSecond).plusMinutes(1)
        },
        nextBusArrivalTime = nextBus?.let {
            time.plusSeconds(it.remainTimeSecond).plusMinutes(1)
        },
        busNumber = nowBus?.busNumber,
        criteria = time
    )
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