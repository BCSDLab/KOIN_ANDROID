package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.DiningApi
import `in`.koreatech.koin.data.response.DiningResponse
import javax.inject.Inject

class DiningRemoteDataSource @Inject constructor(
    private val diningApi: DiningApi,
) {
    suspend fun getDining(date: String): List<DiningResponse> {
        return diningApi.getDining(date)
    }
}