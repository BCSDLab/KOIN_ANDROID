package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.DiningApi
import `in`.koreatech.koin.data.api.auth.DiningAuthApi
import `in`.koreatech.koin.data.request.dining.DiningSoldOutReportRequest
import `in`.koreatech.koin.data.response.DiningResponse
import javax.inject.Inject

class DiningRemoteDataSource @Inject constructor(
    private val diningApi: DiningApi,
    private val diningAuthApi: DiningAuthApi
) {
    suspend fun getDining(date: String): List<DiningResponse> {
        return diningApi.getDining(date)
    }

    suspend fun postDiningSoldOutReport(diningId: Int, imageUrl: String) {
        diningAuthApi.postDiningSoldOutReport(diningId, DiningSoldOutReportRequest(imageUrl))
    }
}
