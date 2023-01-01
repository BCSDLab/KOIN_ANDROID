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

fun BusCourseResponse.toBusCourse() = BusCourse(
    busType = busType.toBusType(),
    direction = direction.toBusDirection(),
    region = region
)

fun BusTimetableResponse.toShuttleBusRoute(): BusRoute.ShuttleBusRoute {
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

fun BusResponse.toShuttleBusArrivalInfo(departure: BusNode, arrival: BusNode) = nowTime.let { time ->
    BusArrivalInfo.ShuttleBusArrivalInfo(
        departure = departure,
        arrival = arrival,
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


fun BusResponse.toCommutingBusRemainTimePair(departure: BusNode, arrival: BusNode) = nowTime.let { time ->
    BusArrivalInfo.CommutingBusArrivalInfo(
        departure = departure,
        arrival = arrival,
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

fun BusResponse.toExpressBusRemainTimePair(departure: BusNode, arrival: BusNode) = nowTime.let { time ->
    BusArrivalInfo.ExpressBusArrivalInfo(
        departure = departure,
        arrival = arrival,
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

fun BusResponse.toCityBusRemainTimePair(departure: BusNode, arrival: BusNode) = nowTime.let { time ->
    BusArrivalInfo.CityBusArrivalInfo(
        departure = departure,
        arrival = arrival,
        nowBusRemainTime = nowBus?.remainTimeSecond,
        nextBusRemainTime = nextBus?.remainTimeSecond,
        nowBusArrivalTime = nowBus?.let {
            time.plusSeconds(it.remainTimeSecond).plusMinutes(1)
        },
        nextBusArrivalTime = nextBus?.let {
            time.plusSeconds(it.remainTimeSecond).plusMinutes(1)
        },
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