package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.response.land.LandDetailResponse
import `in`.koreatech.koin.data.response.land.LandsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface LandApi {
    @GET("lands")
    suspend fun getLandList(): LandsResponse

    @GET("lands/{id}")
    suspend fun getLandDetail(
        @Path("id") id: Int
    ): LandDetailResponse
}
