package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.request.store.StoreReviewReportsRequest
import `in`.koreatech.koin.data.request.user.ABTestRequest
import `in`.koreatech.koin.data.request.user.DeviceTokenRequest
import `in`.koreatech.koin.data.request.user.GeneralUserRequest
import `in`.koreatech.koin.data.request.user.NewPasswordRequest
import `in`.koreatech.koin.data.request.user.PasswordRequest
import `in`.koreatech.koin.data.request.user.ReviewRequest
import `in`.koreatech.koin.data.request.user.StudentUserRequest
import `in`.koreatech.koin.data.response.notification.NotificationPermissionInfoResponse
import `in`.koreatech.koin.data.response.store.StoreReviewResponse
import `in`.koreatech.koin.data.response.user.ABTestResponse
import `in`.koreatech.koin.data.response.user.ABTestTokenResponse
import `in`.koreatech.koin.data.response.user.GeneralUserResponse
import `in`.koreatech.koin.data.response.user.StudentUserResponse
import `in`.koreatech.koin.data.response.user.UserInfoEditResponse
import `in`.koreatech.koin.data.response.user.UserTypeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserAuthApi {
    @GET("user/student/me")
    suspend fun getStudentUser(): StudentUserResponse

    @GET("v2/users/me")
    suspend fun getGeneralUser(): GeneralUserResponse

    @PUT("v2/users/students/me")
    suspend fun putStudentUser(
        @Body studentUserRequest: StudentUserRequest
    ): StudentUserResponse

    @PUT("v2/users/me")
    suspend fun putGeneralUser(
        @Body generalUserRequest: GeneralUserRequest
    ): GeneralUserResponse

    @PUT("users/password")
    suspend fun updateUserPassword(
        @Body newPasswordRequest: NewPasswordRequest
    )

    @DELETE("user")
    suspend fun deleteUser(): Response<Unit?>

    @GET("user/check/nickname/{nickname}")
    suspend fun checkNickName(): UserInfoEditResponse

    @GET("user/auth")
    suspend fun getUserType(): UserTypeResponse

    @POST("/notification")
    suspend fun updateDeviceToken(
        @Body deviceTokenRequest: DeviceTokenRequest
    )

    @GET("/notification")
    suspend fun getNotificationPermissionInfo(): NotificationPermissionInfoResponse

    @POST("/notification/subscribe")
    suspend fun updateSubscription(
        @Query("type") type: String
    )

    @POST("/notification/subscribe/detail")
    suspend fun updateSubscriptionDetail(
        @Query("detail_type") type: String
    )

    @DELETE("/notification/subscribe")
    suspend fun deleteSubscription(
        @Query("type") type: String
    ): Response<Unit?>

    @DELETE("/notification/subscribe/detail")
    suspend fun deleteSubscriptionDetail(
        @Query("detail_type") type: String
    ): Response<Unit?>

    @DELETE("/notification")
    suspend fun deleteDeviceToken(): Response<Unit?>

    @POST("user/check/password")
    suspend fun checkPassword(
        @Body passwordRequest: PasswordRequest
    )

    @GET("shops/{id}/reviews")
    suspend fun getShopReviewsWithAuth(
        @Path("id") uid: Int
    ): StoreReviewResponse

    @POST("shops/{shopId}/reviews")
    suspend fun writeReview(
        @Path("shopId") shopId: Int,
        @Body reviewRequest: ReviewRequest
    )

    @DELETE("shops/{shopId}/reviews/{reviewId}")
    suspend fun deleteReview(
        @Path("reviewId") reviewId: Int,
        @Path("shopId") shopId: Int
    ): Response<Unit?>

    @PUT("shops/{shopId}/reviews/{reviewId}")
    suspend fun modifyReview(
        @Path("reviewId") reviewId: Int,
        @Path("shopId") shopId: Int,
        @Body reviewRequest: ReviewRequest
    ): Response<Unit?>

    @POST("shops/{shopId}/reviews/{reviewId}/reports")
    suspend fun postStoreReviewReports(
        @Path("shopId") shopId: Int,
        @Path("reviewId") reviewId: Int,
        @Body storeReviewReportsRequest: StoreReviewReportsRequest
    ): Response<Unit?>

    @GET("owner")
    suspend fun getOwnerTokenIsValid()

    @POST("abtest/assign/token")
    suspend fun updateABTestToken(): ABTestTokenResponse

    @POST("abtest/assign")
    suspend fun postABTestAssign(
        @Body abTestRequest: ABTestRequest
    ): ABTestResponse

    @POST("shops/{shopId}}/call-notification")
    suspend fun postReviewPromptNotification(
        @Path("storeId") storeId: Int
    )
}