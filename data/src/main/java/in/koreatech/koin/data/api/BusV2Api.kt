package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.response.bus.v2.BusNoticeResponse
import retrofit2.http.GET

interface BusV2Api {

    @GET("bus/notice")
    suspend fun fetchBusNotice(): BusNoticeResponse
}