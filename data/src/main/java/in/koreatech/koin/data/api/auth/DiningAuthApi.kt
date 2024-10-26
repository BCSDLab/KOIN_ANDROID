package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.response.DiningResponse
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Query

interface DiningAuthApi {
    @PATCH(URLConstant.DINING.LIKE)
    suspend fun likeDining(@Query("diningId") id: Int)

    @PATCH(URLConstant.DINING.UNLIKE)
    suspend fun unlikeDining(@Query("diningId") id: Int)

    @GET(URLConstant.DINING.DININGS)
    suspend fun getDining(@Query("date") date: String): List<DiningResponse>
}