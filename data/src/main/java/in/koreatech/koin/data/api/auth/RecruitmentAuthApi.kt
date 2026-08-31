package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.response.recruitment.MyRecruitmentListResponse
import `in`.koreatech.koin.data.response.recruitment.TeamRecruitmentDetailResponse
import `in`.koreatech.koin.data.response.recruitment.TeamRecruitmentListResponse
import `in`.koreatech.koin.data.response.recruitment.TeamRecruitmentNotificationListResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RecruitmentAuthApi {
    @Suppress("LongParameterList")
    @GET("/team-recruitments")
    suspend fun getRecruitments(
        @Query("keyword") keyword: String? = null,
        @Query("status") status: String? = null,
        @Query("categories") categories: List<String>? = null,
        @Query("meetingType") meetingType: String? = null,
        @Query("sort") sort: String? = null,
        @Query("page") page: Int? = 1,
        @Query("limit") limit: Int? = 10
    ): TeamRecruitmentListResponse

    @GET("/team-recruitments/{recruitmentId}")
    suspend fun getRecruitmentDetail(
        @Path("recruitmentId") recruitmentId: Int
    ): TeamRecruitmentDetailResponse

    @DELETE("/team-recruitments/{recruitmentId}")
    suspend fun deleteRecruitment(
        @Path("recruitmentId") recruitmentId: Int
    ): Response<Unit>

    @GET("/team-recruitments/notifications")
    suspend fun getNotifications(
        @Query("page") page: Int? = 1,
        @Query("limit") limit: Int? = 10
    ): TeamRecruitmentNotificationListResponse

    @DELETE("/team-recruitments/notifications")
    suspend fun deleteAllNotifications(): Response<Unit>

    @GET("team-recruitments/me/created")
    suspend fun getMyRecruitmentPosts(
        @Query("status") status: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): MyRecruitmentListResponse

    @POST("/team-recruitments/notifications/{notificationId}/read")
    suspend fun readNotification(
        @Path("notificationId") notificationId: Int
    ): Response<Unit>

    @PUT("team-recruitments/{recruitmentId}/close")
    suspend fun closeRecruitmentPost(
        @Path("recruitmentId") postId: Int
    ): Response<Unit>

    @POST("/team-recruitments/notifications/mark-all-read")
    suspend fun readAllNotifications(): Response<Unit>
}
