package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.request.dining.DiningSoldOutReportRequest
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface DiningAuthApi {
    @POST("/dinings/{diningId}/soldout-reports")
    suspend fun postDiningSoldOutReport(
        @Path("diningId") diningId: Int,
        @Body request: DiningSoldOutReportRequest
    )
}