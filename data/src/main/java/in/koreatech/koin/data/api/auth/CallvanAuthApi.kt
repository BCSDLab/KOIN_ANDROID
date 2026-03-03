package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.request.callvan.CallvanChatMessageRequest
import `in`.koreatech.koin.data.request.callvan.CallvanPostCreateRequest
import `in`.koreatech.koin.data.request.callvan.CallvanUserReportCreateRequest
import `in`.koreatech.koin.data.response.callvan.CallvanChatMessageResponse
import `in`.koreatech.koin.data.response.callvan.CallvanNotificationResponse
import `in`.koreatech.koin.data.response.callvan.CallvanPostCreateResponse
import `in`.koreatech.koin.data.response.callvan.CallvanPostDetailResponse
import `in`.koreatech.koin.data.response.callvan.CallvanPostSearchResponse
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
        @Query("author") author: String?,
        @Query("departures") departures: List<String>?,
        @Query("departure_keyword") departureKeyword: String?,
        @Query("arrivals") arrivals: List<String>?,
        @Query("arrival_keyword") arrivalKeyword: String?,
        @Query("statuses") statuses: List<String>?,
        @Query("title") title: String?,
        @Query("sort") sort: String?,
        @Query("page") page: Int?,
        @Query("limit") limit: Int?
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
    )

    @GET("/callvan/notifications")
    suspend fun getNotifications(): List<CallvanNotificationResponse>

    @DELETE("/callvan/notifications")
    suspend fun deleteAllNotifications()

    @DELETE("/callvan/notifications/{notificationId}")
    suspend fun deleteNotification(
        @Path("notificationId") notificationId: Int
    )

    @POST("/callvan/notifications/{notificationId}/read")
    suspend fun markNotificationAsRead(
        @Path("notificationId") notificationId: Int
    )

    @POST("/callvan/notifications/mark-all-read")
    suspend fun markAllNotificationsAsRead()

    @PUT("/callvan/posts/{postId}/complete")
    suspend fun completeCallvanPost(
        @Path("postId") postId: Int
    )

    @POST("/callvan/posts/{postId}/participants")
    suspend fun joinCallvanPost(
        @Path("postId") postId: Int
    )

    @DELETE("/callvan/posts/{postId}/participants")
    suspend fun leaveCallvanPost(
        @Path("postId") postId: Int
    )

    @PUT("/callvan/posts/{postId}/reopen")
    suspend fun reopenCallvanPost(
        @Path("postId") postId: Int
    )
}
