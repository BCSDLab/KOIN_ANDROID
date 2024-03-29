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
    ): ShuttleBusTimetableResponse {
        return busApi.getShuttleBusTimetable("shuttle", busDirection, region)
    }

    suspend fun getCommutingBusTimetable(
        busDirection: String,
        region: String
    ): ShuttleBusTimetableResponse {
        return busApi.getShuttleBusTimetable("commuting", busDirection, region)
    }

    suspend fun getExpressBusTimetable(
        busDirection: String
    ): ExpressBusTimetableResponse {
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