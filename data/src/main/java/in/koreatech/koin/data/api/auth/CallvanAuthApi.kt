package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.request.callvan.CallvanChatMessageRequest
import `in`.koreatech.koin.data.request.callvan.CallvanPostCreateRequest
import `in`.koreatech.koin.data.request.callvan.CallvanUserReportCreateRequest
import `in`.koreatech.koin.data.response.callvan.CallvanChatMessageResponse
import `in`.koreatech.koin.data.response.callvan.CallvanNotificationResponse
import `in`.koreatech.koin.data.response.callvan.CallvanPostCreateResponse
import `in`.koreatech.koin.data.response.callvan.CallvanPostDetailResponse
import `in`.koreatech.koin.data.response.callvan.CallvanPostSearchResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface CallvanAuthApi {
    @POST("/callvan")
    suspend fun createCallvanPost(
        @Body callvanPostCreateRequest: CallvanPostCreateRequest
    ): CallvanPostCreateResponse

    @GET("/callvan")
    suspend fun getCallvanPosts(
        @Query("author") author: String? = "ALL",
        @Query("departures") departures: List<String>? = null,
        @Query("departure_keyword") departureKeyword: String? = null,
        @Query("arrivals") arrivals: List<String>? = null,
        @Query("arrival_keyword") arrivalKeyword: String? = null,
        @Query("statuses") statuses: List<String>? = null,
        @Query("title") title: String? = null,
        @Query("sort") sort: String? = "LATEST_DESC",
        @Query("page") page: Int? = 1,
        @Query("limit") limit: Int? = 10
    ): CallvanPostSearchResponse

    @POST("/callvan/posts/{postId}/chat")
    suspend fun sendMessage(
        @Path("postId") postId: Int,
        @Body callvanChatMessageRequest: CallvanChatMessageRequest
    )

    @GET("/callvan/posts/{postId}/chat")
    suspend fun getCallvanChatMessages(
        @Path("postId") postId: Int
    ): CallvanChatMessageResponse

    @GET("/callvan/posts/{postId}")
    suspend fun getCallvanPostDetail(
        @Path("postId") postId: Int
    ): CallvanPostDetailResponse

    @POST("/callvan/posts/{postId}/reports")
    suspend fun reportCallvanUser(
        @Path("postId") postId: Int,
        @Body callvanUserReportCreateRequest: CallvanUserReportCreateRequest
    )

    @PUT("/callvan/posts/{postId}/close")
    suspend fun closeCallvanPost(
        @Path("postId") postId: Int
    ): Response<Unit>

    @GET("/callvan/notifications")
    suspend fun getNotifications(): List<CallvanNotificationResponse>

    @DELETE("/callvan/notifications")
    suspend fun deleteAllNotifications(): Response<Unit>

    @DELETE("/callvan/notifications/{notificationId}")
    suspend fun deleteNotification(
        @Path("notificationId") notificationId: Int
    ): Response<Unit>

    @POST("/callvan/notifications/{notificationId}/read")
    suspend fun markNotificationAsRead(
        @Path("notificationId") notificationId: Int
    ): Response<Unit>

    @POST("/callvan/notifications/mark-all-read")
    suspend fun markAllNotificationsAsRead(): Response<Unit>

    @PUT("/callvan/posts/{postId}/complete")
    suspend fun completeCallvanPost(
        @Path("postId") postId: Int
    ): Response<Unit>

    @POST("/callvan/posts/{postId}/participants")
    suspend fun joinCallvanPost(
        @Path("postId") postId: Int
    )

    @DELETE("/callvan/posts/{postId}/participants")
    suspend fun leaveCallvanPost(
        @Path("postId") postId: Int
    ): Response<Unit>

    @PUT("/callvan/posts/{postId}/reopen")
    suspend fun reopenCallvanPost(
        @Path("postId") postId: Int
    ): Response<Unit>
}
