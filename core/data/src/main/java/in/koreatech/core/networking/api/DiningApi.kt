package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.core.networking.response.dining.DiningResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DiningApi {
    @GET(URLConstant.DINING)
    suspend fun getDining(@Query("date") date: String): List<DiningResponse>
}