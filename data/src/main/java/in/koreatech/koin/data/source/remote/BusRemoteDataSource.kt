package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.BusApi
import `in`.koreatech.koin.data.response.bus.*
import javax.inject.Inject

class BusRemoteDataSource @Inject constructor(
    private val busApi: BusApi
) {
    suspend fun getBusCourses(): List<BusCourseResponse> {
        return busApi.getBusCourses()
    }

    suspend fun getShuttleBusTimetable(
        busDirection: String,
        region: String
    ): List<ShuttleBusRouteResponse> {
        return busApi.getBusTimetable("shuttle", busDirection, region)
    }

    suspend fun getCommutingBusTimetable(
        busDirection: String,
        region: String
    ): List<ShuttleBusRouteResponse> {
        return busApi.getBusTimetable("commuting", busDirection, region)
    }

    suspend fun getExpressBusTimetable(
        busDirection: String
    ): List<ExpressBusRouteResponse> {
        return busApi.getExpressBusTimetable(busDirection, "")
    }

    suspend fun searchBus(
        date: String, // yyyy-MM-dd
        time: String, // HH:mm
        departure: String,
        arrival: String
    ) : List<BusSearchResponse> {
        return busApi.searchBus(date, time, departure, arrival)
    }

    suspend fun getBuses(
        busType: String,
        departure: String,
        arrival: String
    ): BusResponse {
        return busApi.getBus(busType, departure, arrival)
    }
}