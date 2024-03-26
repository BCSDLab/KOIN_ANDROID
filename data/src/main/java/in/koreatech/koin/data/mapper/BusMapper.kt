package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.R
import `in`.koreatech.koin.data.response.bus.*
import `in`.koreatech.koin.data.util.nowTime
import `in`.koreatech.koin.domain.model.bus.BusNode
import `in`.koreatech.koin.domain.model.bus.course.BusCourse
import `in`.koreatech.koin.domain.model.bus.search.BusSearchResult
import `in`.koreatech.koin.domain.model.bus.timer.BusArrivalInfo
import `in`.koreatech.koin.domain.model.bus.timetable.BusNodeInfo
import `in`.koreatech.koin.domain.model.bus.timetable.BusRoute
import `in`.koreatech.koin.domain.model.bus.toBusDirection
import `in`.koreatech.koin.domain.model.bus.toBusType
import android.content.Context
import `in`.koreatech.koin.domain.model.bus.timetable.BusTimetable

fun BusCourseResponse.toBusCourse() = BusCourse(
    busType = busType.toBusType(),
    direction = direction.toBusDirection(),
    region = region
)

fun ShuttleBusTimetableResponse.toShuttleBusTimetable(): BusTimetable.ShuttleBusTimetable {
    return BusTimetable.ShuttleBusTimetable(
        routes = this.routes.map {
            it.toShuttleBusRoute()
        },
        updatedAt = updatedAt
    )
}

fun ExpressBusTimetableResponse.toExpressBusTimetable(): BusTimetable.ExpressBusTimetable {
    return BusTimetable.ExpressBusTimetable(
        routes = this.routes.toExpressBusRoute(),
        updatedAt = updatedAt
    )
}

fun ShuttleBusRouteResponse.toShuttleBusRoute(): BusRoute.ShuttleBusRoute {
    return BusRoute.ShuttleBusRoute(
        routeName = routeName,
        arrivalInfo = arrivalInfo.map { route ->
            BusNodeInfo.ShuttleNodeInfo(
                node = route.nodeName,
                arrivalTime = route.arrivalTime
            )
        }
    )
}

fun List<ExpressBusRouteResponse>.toExpressBusRoute(): BusRoute.ExpressBusRoute {
    return BusRoute.ExpressBusRoute(
        arrivalInfo = map { route ->
            BusNodeInfo.ExpressNodeInfo(
                departureTime = route.departure,
                arrivalTime = route.arrival,
                charge = route.charge
            )
        }
    )
}

fun List<CityBusRouteResponse>.toCityBusRoute(): BusRoute.CityBusRoute {
    return BusRoute.CityBusRoute(
        arrivalInfo = map { BusNodeInfo.CitybusNodeInfo(it.startBusNode, it.timeInfo) }
    )
}

fun BusResponse.toShuttleBusArrivalInfo(
    departure: BusNode,
    arrival: BusNode
): BusArrivalInfo.ShuttleBusArrivalInfo {
    val time = nowTime
    val nowBusRemainTimeSecond = nowBus?.remainTimeSecond
    val nextBusRemainTimeSecond = nextBus?.remainTimeSecond

    return BusArrivalInfo.ShuttleBusArrivalInfo(
        departure = departure,
        arrival = arrival,
        nowBusRemainTime = nowBusRemainTimeSecond,
        nextBusRemainTime = nextBus?.remainTimeSecond,
        nowBusArrivalTime = nowBusRemainTimeSecond?.let { time.plusSeconds(it).plusMinutes(1) },
        nextBusArrivalTime = nextBusRemainTimeSecond?.let { time.plusSeconds(it).plusMinutes(1) },
        criteria = time
    )
}


fun BusResponse.toCommutingBusRemainTimePair(
    departure: BusNode,
    arrival: BusNode
): BusArrivalInfo.CommutingBusArrivalInfo {
    val time = nowTime
    val nowBusRemainTimeSecond = nowBus?.remainTimeSecond
    val nextBusRemainTimeSecond = nextBus?.remainTimeSecond

    return BusArrivalInfo.CommutingBusArrivalInfo(
        departure = departure,
        arrival = arrival,
        nowBusRemainTime = nowBusRemainTimeSecond,
        nextBusRemainTime = nextBus?.remainTimeSecond,
        nowBusArrivalTime = nowBusRemainTimeSecond?.let { time.plusSeconds(it).plusMinutes(1) },
        nextBusArrivalTime = nextBusRemainTimeSecond?.let { time.plusSeconds(it).plusMinutes(1) },
        criteria = time
    )
}

fun BusResponse.toExpressBusRemainTimePair(
    departure: BusNode,
    arrival: BusNode
): BusArrivalInfo.ExpressBusArrivalInfo {
    val time = nowTime
    val nowBusRemainTimeSecond = nowBus?.remainTimeSecond
    val nextBusRemainTimeSecond = nextBus?.remainTimeSecond

    return BusArrivalInfo.ExpressBusArrivalInfo(
        departure = departure,
        arrival = arrival,
        nowBusRemainTime = nowBusRemainTimeSecond,
        nextBusRemainTime = nextBus?.remainTimeSecond,
        nowBusArrivalTime = nowBusRemainTimeSecond?.let { time.plusSeconds(it).plusMinutes(1) },
        nextBusArrivalTime = nextBusRemainTimeSecond?.let { time.plusSeconds(it).plusMinutes(1) },
        criteria = time
    )
}

fun BusResponse.toCityBusRemainTimePair(
    departure: BusNode,
    arrival: BusNode
): BusArrivalInfo.CityBusArrivalInfo {
    val time = nowTime
    val nowBusRemainTimeSecond = nowBus?.remainTimeSecond
    val nextBusRemainTimeSecond = nextBus?.remainTimeSecond

    return BusArrivalInfo.CityBusArrivalInfo(
        departure = departure,
        arrival = arrival,
        nowBusRemainTime = nowBusRemainTimeSecond,
        nextBusRemainTime = nextBus?.remainTimeSecond,
        nowBusArrivalTime = nowBusRemainTimeSecond?.let { time.plusSeconds(it).plusMinutes(1) },
        nextBusArrivalTime = nextBusRemainTimeSecond?.let { time.plusSeconds(it).plusMinutes(1) },
        busNumber = nowBus?.busNumber ?: 0,
        criteria = time
    )
}

fun BusSearchResponse.toBusSearchResult(context: Context): BusSearchResult {
    return BusSearchResult(
        busType = busName.toBusType(),
        busTimeString = busTime ?: context.getString(R.string.bus_end_information)
    )
}