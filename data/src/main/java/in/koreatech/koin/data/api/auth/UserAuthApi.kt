package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.request.store.StoreReviewReportsRequest
import `in`.koreatech.koin.data.request.user.ABTestRequest
import `in`.koreatech.koin.data.request.user.DeviceTokenRequest
import `in`.koreatech.koin.data.request.user.PasswordRequest
import `in`.koreatech.koin.data.request.user.ReviewRequest
import `in`.koreatech.koin.data.request.user.UserRequest
import `in`.koreatech.koin.data.response.notification.NotificationPermissionInfoResponse
import `in`.koreatech.koin.data.response.store.StoreReviewResponse
import `in`.koreatech.koin.data.response.user.ABTestResponse
import `in`.koreatech.koin.data.response.user.ABTestTokenResponse
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

    @GET(URLConstant.USER.CHECK.BYNICKNAME)
    suspend fun checkNickName(): UserInfoEditResponse

    @GET(URLConstant.USER.AUTH)
    suspend fun getUserType(): UserTypeResponse

    @POST(URLConstant.USER.NOTIFICATION)
    suspend fun updateDeviceToken(
        @Body deviceTokenRequest: DeviceTokenRequest
    )

    @GET(URLConstant.USER.NOTIFICATION)
    suspend fun getNotificationPermissionInfo(): NotificationPermissionInfoResponse

    @POST(URLConstant.USER.SUBSCRIBE)
    suspend fun updateSubscription(
        @Query("type") type: String
    )

    @POST(URLConstant.USER.DETAIL)
    suspend fun updateSubscriptionDetail(
        @Query("detail_type") type: String
    )

    @DELETE(URLConstant.USER.SUBSCRIBE)
    suspend fun deleteSubscription(
        @Query("type") type: String
    ): Response<Unit?>

    @DELETE(URLConstant.USER.DETAIL)
    suspend fun deleteSubscriptionDetail(
        @Query("detail_type") type: String
    ): Response<Unit?>

    @DELETE(URLConstant.USER.NOTIFICATION)
    suspend fun deleteDeviceToken(): Response<Unit?>

    @POST(URLConstant.USER.CHECK.PASSWORD)
    suspend fun checkPassword(
        @Body passwordRequest: PasswordRequest
    )

    @GET(URLConstant.SHOPS.ID.REVIEWS)
    suspend fun getShopReviewsWithAuth(
        @Path("id") uid: Int
    ): StoreReviewResponse

    @POST(URLConstant.SHOPS.SHOPID.REVIEWS.REVIEWS)
    suspend fun writeReview(
        @Path("shopId") shopId: Int,
        @Body reviewRequest: ReviewRequest
    )

    @DELETE(URLConstant.SHOPS.SHOPID.REVIEWS.REVIEWID)
    suspend fun deleteReview(
        @Path("reviewId") reviewId: Int,
        @Path("shopId") shopId: Int
    ): Response<Unit?>

    @PUT(URLConstant.SHOPS.SHOPID.REVIEWS.REVIEWID)
    suspend fun modifyReview(
        @Path("reviewId") reviewId: Int,
        @Path("shopId") shopId: Int,
        @Body reviewRequest: ReviewRequest
    ): Response<Unit?>

    @POST(URLConstant.SHOPS.SHOPID.REVIEWS.REPORTS)
    suspend fun postStoreReviewReports(
        @Path("storeId") storeId: Int,
        @Path("reviewId") reviewId: Int,
        @Body storeReviewReportsRequest: StoreReviewReportsRequest
    ): Response<Unit?>

    @GET(URLConstant.OWNER.OWNER)
    suspend fun getOwnerTokenIsValid()

    @POST(URLConstant.ABTEST.UPDATE)
    suspend fun updateABTestToken(): ABTestTokenResponse

    @POST(URLConstant.ABTEST.ASSIGN)
    suspend fun postABTestAssign(
        @Body abTestRequest: ABTestRequest
    ): ABTestResponse

    @POST(URLConstant.SHOPS.NOTIFICATION)
    suspend fun postReviewPromptNotification(
        @Path("storeId") storeId: Int
    )
}
