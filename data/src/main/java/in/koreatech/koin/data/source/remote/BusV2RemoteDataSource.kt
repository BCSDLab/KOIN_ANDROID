package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.BusV2Api
import `in`.koreatech.koin.data.response.bus.v2.BusNoticeResponse
import javax.inject.Inject

class BusV2RemoteDataSource @Inject constructor(
    private val busApi: BusV2Api
) {

    suspend fun fetchBusNotice(): BusNoticeResponse {
        return busApi.fetchBusNotice()
    }
}