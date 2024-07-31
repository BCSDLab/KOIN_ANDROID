package `in`.koreatech.koin.data.mapper

import android.content.Context
import `in`.koreatech.koin.data.R
import `in`.koreatech.koin.data.response.bus.BusCourseResponse
import `in`.koreatech.koin.data.response.bus.BusResponse
import `in`.koreatech.koin.data.response.bus.BusSearchResponse
import `in`.koreatech.koin.data.response.bus.CityBusDepartTimesResponse
import `in`.koreatech.koin.data.response.bus.CityBusInfoResponse
import `in`.koreatech.koin.data.response.bus.CityBusTimetableResponse
import `in`.koreatech.koin.data.response.bus.ExpressBusRouteResponse
import `in`.koreatech.koin.data.response.bus.ExpressBusTimetableResponse
import `in`.koreatech.koin.data.response.bus.ShuttleBusRouteResponse
import `in`.koreatech.koin.data.response.bus.ShuttleBusTimetableResponse
import `in`.koreatech.koin.data.util.nowTime
import `in`.koreatech.koin.domain.model.bus.BusNode
import `in`.koreatech.koin.domain.model.bus.city.toCityBusDayType
import `in`.koreatech.koin.domain.model.bus.city.toCityBusGeneralDestination
import `in`.koreatech.koin.domain.model.bus.course.BusCourse
import `in`.koreatech.koin.domain.model.bus.search.BusSearchResult
import `in`.koreatech.koin.domain.model.bus.timer.BusArrivalInfo
import `in`.koreatech.koin.domain.model.bus.timetable.BusNodeInfo
import `in`.koreatech.koin.domain.model.bus.timetable.BusRoute
import `in`.koreatech.koin.domain.model.bus.timetable.BusTimetable
import `in`.koreatech.koin.domain.model.bus.toBusDirection
import `in`.koreatech.koin.domain.model.bus.toBusType
import java.time.LocalDate

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

fun CityBusTimetableResponse.toCityBusTimetable(): BusTimetable.CityBusTimetable {
    return BusTimetable.CityBusTimetable(
        busInfos = busInfo.toCityBusNodeInfo(),
        departTimes = departTimes.getTodayDepartTimes(),
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

fun CityBusInfoResponse.toCityBusNodeInfo(): BusNodeInfo.CityBusNodeInfo {
    return BusNodeInfo.CityBusNodeInfo(
        busNumber = number,
        departNode = departNode.toCityBusGeneralDestination,
        arrivalNode = arrivalNode.toCityBusGeneralDestination
    )
}

fun List<CityBusDepartTimesResponse>.getTodayDepartTimes(): List<String> {
    this.map { times ->
        val dayType = times.dayOfWeek.toCityBusDayType
        // 평일 or 주말
        if (dayType.daysOfWeek.contains(LocalDate.now().dayOfWeek)) {
            return times.departInfo
        }
    }
    return this.first().departInfo  // 기본으로 평일 리스트 반환
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