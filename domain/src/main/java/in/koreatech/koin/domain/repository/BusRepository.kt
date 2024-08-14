package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.bus.*
import `in`.koreatech.koin.domain.model.bus.city.CityBusInfo
import `in`.koreatech.koin.domain.model.bus.course.BusCourse
import `in`.koreatech.koin.domain.model.bus.search.BusSearchResult
import `in`.koreatech.koin.domain.model.bus.timer.BusArrivalInfo
import `in`.koreatech.koin.domain.model.bus.timetable.BusRoute
import `in`.koreatech.koin.domain.model.bus.timetable.BusTimetable
import java.time.LocalDateTime

interface BusRepository {
    suspend fun getShuttleBusCourses(): List<Pair<BusCourse, String>>
    suspend fun getExpressBusCourses(): List<Pair<BusCourse, String>>
    suspend fun getShuttleBusTimetable(busCourse: BusCourse): BusTimetable.ShuttleBusTimetable
    suspend fun getExpressBusTimetable(busCourse: BusCourse): BusTimetable.ExpressBusTimetable
    suspend fun getCityBusTimetable(cityBusInfo: CityBusInfo): BusTimetable.CityBusTimetable

    suspend fun getShuttleBusRemainTime(
        departure: BusNode,
        arrival: BusNode
    ): BusArrivalInfo.ShuttleBusArrivalInfo

    suspend fun getExpressBusRemainTime(
        departure: BusNode,
        arrival: BusNode
    ): BusArrivalInfo.ExpressBusArrivalInfo

    suspend fun getCommutingBusRemainTime(
        departure: BusNode,
        arrival: BusNode
    ): BusArrivalInfo.CommutingBusArrivalInfo

    suspend fun getCityBusRemainTime(
        departure: BusNode,
        arrival: BusNode
    ): BusArrivalInfo.CityBusArrivalInfo

    suspend fun searchBus(
        time: LocalDateTime,
        departure: BusNode,
        arrival: BusNode
    ): List<BusSearchResult>
}