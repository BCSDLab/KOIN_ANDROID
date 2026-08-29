package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.response.recruitment.RecruitmentListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RecruitmentAuthApi {

    @GET("team-recruitments/me/created")
    suspend fun getMyRecruitmentPosts(
        @Query("status") status: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): RecruitmentListResponse

    @POST("team-recruitments/{id}/close")
    suspend fun closeRecruitmentPost(
        @Path("id") postId: Int
    ): Response<Unit>
}
