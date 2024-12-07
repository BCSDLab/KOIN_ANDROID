package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.response.bus.v2.BusNoticeResponse
import `in`.koreatech.koin.data.response.bus.v2.ShuttleTimetableResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query

interface BusV2Api {

    @GET("bus/notice")
    suspend fun fetchBusNotice(): BusNoticeResponse

    @GET("bus/timetable/shuttle")
    suspend fun fetchShuttleTimetable(
        @Query("id") id: String
    ): ShuttleTimetableResponse
}