package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.response.land.LandDetailResponse
import `in`.koreatech.koin.data.response.land.LandsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface LandApi {
    @GET(URLConstant.LAND)
    suspend fun getLandList(): LandsResponse

    @GET("${URLConstant.LAND}/{id}")
    suspend fun getLandDetail(@Path("id") id: Int): LandDetailResponse
}