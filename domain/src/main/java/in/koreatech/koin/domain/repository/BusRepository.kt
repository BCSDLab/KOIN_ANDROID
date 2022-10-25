package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.bus.*
import java.time.LocalDateTime

interface BusRepository {
    suspend fun getBusCourses(): List<BusCourse>

    suspend fun getShuttleBusTimetable(
        busDirection: BusDirection,
        region: String
    ): List<BusRoute.ShuttleBusRoute>

    suspend fun getExpressBusTimetable(
        busDirection: BusDirection,
        region: String
    ): BusRoute.ExpressBusRoute

    suspend fun getCommutingBusTimetable(
        busDirection: BusDirection,
        region: String
    ): List<BusRoute.CommutingBusRoute>

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