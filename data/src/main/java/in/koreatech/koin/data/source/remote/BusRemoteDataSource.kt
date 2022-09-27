package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.BusApi
import `in`.koreatech.koin.data.response.bus.BusCourseResponse
import `in`.koreatech.koin.data.response.bus.NextBusResponse
import `in`.koreatech.koin.data.response.bus.express.ExpressBusTimetableResponse
import `in`.koreatech.koin.data.response.bus.express.express
import `in`.koreatech.koin.data.response.bus.shuttle.ShuttleBusTimetableResponse
import `in`.koreatech.koin.data.response.bus.shuttle.shuttle
import javax.inject.Inject

class BusRemoteDataSource @Inject constructor(
    private val busApi: BusApi
) {
    suspend fun getBusCourses(): List<BusCourseResponse> {
        return busApi.getBusCourses()
    }

    suspend fun getShuttleBusTimetable(
        region: String
    ) : ShuttleBusTimetableResponse {
        return busApi.getBusTimetable("shuttle", region).shuttle
    }

    suspend fun getCommutingBusTimetable(
        region: String
    ) : ShuttleBusTimetableResponse {
        return busApi.getBusTimetable("commuting", region).shuttle
    }

    suspend fun getExpressBusTimetable(
        region: String
    ) : ExpressBusTimetableResponse {
        return busApi.getBusTimetable("express", region).express
    }

    suspend fun getBuses(
        departure: String,
        arrival: String
    ): NextBusResponse {
        return busApi.getBuses(departure, arrival)
    }
}