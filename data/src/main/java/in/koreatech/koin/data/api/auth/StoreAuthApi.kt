package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.request.user.ReviewRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface StoreAuthApi {
    @POST("/shops/{shopId}/reviews")
    suspend fun writeReview(
        @Path("shopId") shopId: Int,
        @Body reviewRequest: ReviewRequest
    ): Response<Unit?>
}
