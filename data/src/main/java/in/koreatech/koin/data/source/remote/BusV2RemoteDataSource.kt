package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.BusV2Api
import `in`.koreatech.koin.data.response.bus.v2.BusNoticeResponse
import `in`.koreatech.koin.data.response.bus.v2.CityTimetableResponse
import `in`.koreatech.koin.data.response.bus.v2.ExpressTimetableResponse
import `in`.koreatech.koin.data.response.bus.v2.ShuttleCoursesResponse
import `in`.koreatech.koin.data.response.bus.v2.ShuttleTimetableResponse
import javax.inject.Inject

class BusV2RemoteDataSource @Inject constructor(
    private val busApi: BusV2Api
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

    suspend fun fetchCityTimetable(busNumber: Int, direction: String): CityTimetableResponse {
        return busApi.fetchCityTimetable(busNumber, direction)
    }
}