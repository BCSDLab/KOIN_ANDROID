package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.CoopShopApi
import `in`.koreatech.koin.data.response.coopshop.CoopShopResponse
import javax.inject.Inject

class CoopShopRemoteDataSource @Inject constructor(
    private val coopShopApi: CoopShopApi
) {
    suspend fun getCoopShopAll(): List<CoopShopResponse> {
        return coopShopApi.getCoopShopAll()
    }

    suspend fun getCoopShopById(id: Int): CoopShopResponse {
        return coopShopApi.getCoopShopById(id)
    }
}