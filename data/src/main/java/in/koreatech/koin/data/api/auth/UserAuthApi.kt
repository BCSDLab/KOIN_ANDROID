package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.request.store.StoreReviewReportsRequest
import `in`.koreatech.koin.data.request.user.ABTestRequest
import `in`.koreatech.koin.data.request.user.DeviceTokenRequest
import `in`.koreatech.koin.data.request.user.GeneralInfoRequest
import `in`.koreatech.koin.data.request.user.PasswordRequest
import `in`.koreatech.koin.data.request.user.ReviewRequest
import `in`.koreatech.koin.data.request.user.SmsVerifyRequest
import `in`.koreatech.koin.data.request.user.UserRequest
import `in`.koreatech.koin.data.response.notification.NotificationPermissionInfoResponse
import `in`.koreatech.koin.data.response.store.StoreReviewResponse
import `in`.koreatech.koin.data.response.user.ABTestResponse
import `in`.koreatech.koin.data.response.user.ABTestTokenResponse
import `in`.koreatech.koin.data.response.user.TokenResponse
import `in`.koreatech.koin.data.response.user.UserInfoEditResponse
import `in`.koreatech.koin.data.response.user.UserResponse
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
    @GET(URLConstant.USER.ME)
    suspend fun getUser(): UserResponse

    @PUT(URLConstant.USER.ME)
    suspend fun putUser(
        @Body userRequest: UserRequest
    ): UserResponse

    @DELETE(URLConstant.USER.USER)
    suspend fun deleteUser(): Response<Unit?>

    @GET(URLConstant.USER.CHECKNICKNAME + "/{nickname}")
    suspend fun checkNickName(): UserInfoEditResponse

    @GET(URLConstant.USER.AUTH)
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

    @POST(URLConstant.USER.CHECKPASSWORD)
    suspend fun checkPassword(
        @Body passwordRequest: PasswordRequest
    )

    @GET(URLConstant.SHOPS.SHOPS + "/{id}" + "/reviews")
    suspend fun getShopReviewsWithAuth(
        @Path("id") uid: Int
    ): StoreReviewResponse

    @POST("/shops/{shopId}/reviews")
    suspend fun writeReview(
        @Path("shopId") shopId: Int,
        @Body reviewRequest: ReviewRequest
    )

    @DELETE("/shops/{shopId}/reviews/{reviewId}")
    suspend fun deleteReview(
        @Path("reviewId") reviewId: Int,
        @Path("shopId") shopId: Int
    ): Response<Unit?>

    @PUT("/shops/{shopId}/reviews/{reviewId}")
    suspend fun modifyReview(
        @Path("reviewId") reviewId: Int,
        @Path("shopId") shopId: Int,
        @Body reviewRequest: ReviewRequest
    ): Response<Unit?>

    @POST(URLConstant.SHOPS.SHOPS + "/{storeId}" + "/reviews" + "/{reviewId}" + "/reports")
    suspend fun postStoreReviewReports(
        @Path("storeId") storeId: Int,
        @Path("reviewId") reviewId: Int,
        @Body storeReviewReportsRequest: StoreReviewReportsRequest
    ): Response<Unit?>

    @GET(URLConstant.OWNER.OWNER)
    suspend fun getOwnerTokenIsValid()

    @POST("abtest/assign/token")
    suspend fun updateABTestToken(): ABTestTokenResponse

    @POST("abtest/assign")
    suspend fun postABTestAssign(
        @Body abTestRequest: ABTestRequest
    ): ABTestResponse

    @POST(URLConstant.SHOPS.SHOPS + "/{storeId}/call-notification")
    suspend fun postReviewPromptNotification(
        @Path("storeId") storeId: Int
    )

    @POST(URLConstant.USER.SMSVERIFY)
    suspend fun codeVerify(
        @Body smsVerifyRequest: SmsVerifyRequest
    ) : TokenResponse
}
