package `in`.koreatech.koin.data.source.remote

import android.util.Log
import `in`.koreatech.koin.data.api.DiningApi
import `in`.koreatech.koin.data.api.auth.DiningAuthApi
import `in`.koreatech.koin.data.response.DiningResponse
import javax.inject.Inject

class DiningRemoteDataSource @Inject constructor(
    private val diningApi: DiningApi,
    private val diningAuthApi: DiningAuthApi
) {
    suspend fun getDining(date: String): List<DiningResponse> {
        return diningApi.getDining(date)
    }

    suspend fun getAuthDining(date: String): List<DiningResponse> {
        return diningAuthApi.getDining(date)
    }

    suspend fun likeDining(id: Int) {
        diningAuthApi.likeDining(id)
    }
    suspend fun unlikeDining(id: Int) {
        diningAuthApi.unlikeDining(id)
    }
}