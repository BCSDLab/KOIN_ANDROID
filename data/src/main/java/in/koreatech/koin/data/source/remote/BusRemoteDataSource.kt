package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.BusApi
import `in`.koreatech.koin.data.response.bus.BusNoticeResponse
import `in`.koreatech.koin.data.response.bus.BusSearchResultWrapperResponse
import `in`.koreatech.koin.data.response.bus.CityTimetableResponse
import `in`.koreatech.koin.data.response.bus.ExpressTimetableResponse
import `in`.koreatech.koin.data.response.bus.ShuttleCoursesResponse
import `in`.koreatech.koin.data.response.bus.ShuttleTimetableResponse
import javax.inject.Inject

class BusRemoteDataSource @Inject constructor(
    private val busApi: BusApi
) {
    suspend fun fetchBusNotice(): BusNoticeResponse {
        return busApi.fetchBusNotice()
    }

    suspend fun fetchShuttleTimetable(id: String): ShuttleTimetableResponse {
        return busApi.fetchShuttleTimetable(id)
    }

    suspend fun fetchShuttleCourses(): ShuttleCoursesResponse {
        return busApi.fetchShuttleCourses()
    }

    suspend fun fetchExpressTimetable(direction: String): ExpressTimetableResponse {
        return busApi.fetchExpressTimetable(direction)
    }

    suspend fun fetchCityTimetable(
        busNumber: Int,
        direction: String
    ): CityTimetableResponse {
        return busApi.fetchCityTimetable(busNumber, direction)
    }

    suspend fun fetchBusSearchResult(
        date: String,
        time: String,
        busType: String,
        departure: String,
        arrival: String
    ): BusSearchResultWrapperResponse {
        return busApi.fetchBusSearchResult(
            date = date,
            time = time,
            busType = busType,
            departure = departure,
            arrival = arrival
        )
    }
}
