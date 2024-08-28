package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.response.coopshop.CoopShopResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CoopShopApi {
    @GET(URLConstant.COOPSHOP)
    suspend fun getCoopShopAll(): List<CoopShopResponse>

    @GET("${URLConstant.COOPSHOP}/{coopShopId}")
    suspend fun getCoopShopById(@Path("coopShopId") id: Int): CoopShopResponse
}