package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.response.coopshop.CoopShopResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CoopShopApi {
    @GET("coopshop")
    suspend fun getCoopShopAll(): List<CoopShopResponse>

    @GET("coopshop/{coopShopId}")
    suspend fun getCoopShopById(
        @Path("coopShopId") id: Int
    ): CoopShopResponse
}
