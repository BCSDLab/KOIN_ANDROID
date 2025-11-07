package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.constant.URLConstant
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
    @GET(URLConstant.USER.ME)
    suspend fun getStudentUser(): StudentUserResponse

    @GET(URLConstant.USERS.GENERAL.ME)
    suspend fun getGeneralUser(): GeneralUserResponse

    @PUT(URLConstant.USERS.STUDENTS.ME)
    suspend fun putStudentUser(
        @Body studentUserRequest: StudentUserRequest
    ): StudentUserResponse

    @PUT(URLConstant.USERS.GENERAL.ME)
    suspend fun putGeneralUser(
        @Body generalUserRequest: GeneralUserRequest
    ): GeneralUserResponse

    @PUT(URLConstant.USERS.PASSWORD_CHANGE)
    suspend fun updateUserPassword(
        @Body newPasswordRequest: NewPasswordRequest
    )

    @DELETE(URLConstant.USER.USER)
    suspend fun deleteUser(): Response<Unit?>

    @GET(URLConstant.USER.CHECK.BYNICKNAME)
    suspend fun checkNickName(): UserInfoEditResponse

    @GET(URLConstant.USER.AUTH)
    suspend fun getUserType(): UserTypeResponse

    @POST(URLConstant.USER.NOTIFICATION.NOTIFICATION)
    suspend fun updateDeviceToken(
        @Body deviceTokenRequest: DeviceTokenRequest
    )

    @GET(URLConstant.USER.NOTIFICATION.NOTIFICATION)
    suspend fun getNotificationPermissionInfo(): NotificationPermissionInfoResponse

    @POST(URLConstant.USER.NOTIFICATION.SUBSCRIBE.SUBSCRIBE)
    suspend fun updateSubscription(
        @Query("type") type: String
    )

    @POST(URLConstant.USER.NOTIFICATION.SUBSCRIBE.DETAIL)
    suspend fun updateSubscriptionDetail(
        @Query("detail_type") type: String
    )

    @DELETE(URLConstant.USER.NOTIFICATION.SUBSCRIBE.SUBSCRIBE)
    suspend fun deleteSubscription(
        @Query("type") type: String
    ): Response<Unit?>

    @DELETE(URLConstant.USER.NOTIFICATION.SUBSCRIBE.DETAIL)
    suspend fun deleteSubscriptionDetail(
        @Query("detail_type") type: String
    ): Response<Unit?>

    @DELETE(URLConstant.USER.NOTIFICATION.NOTIFICATION)
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

    @DELETE(URLConstant.SHOPS.SHOPID.REVIEWS.REVIEWID.REVIEWID)
    suspend fun deleteReview(
        @Path("reviewId") reviewId: Int,
        @Path("shopId") shopId: Int
    ): Response<Unit?>

    @PUT(URLConstant.SHOPS.SHOPID.REVIEWS.REVIEWID.REVIEWID)
    suspend fun modifyReview(
        @Path("reviewId") reviewId: Int,
        @Path("shopId") shopId: Int,
        @Body reviewRequest: ReviewRequest
    ): Response<Unit?>

    @POST(URLConstant.SHOPS.SHOPID.REVIEWS.REVIEWID.REPORTS)
    suspend fun postStoreReviewReports(
        @Path("shopId") shopId: Int,
        @Path("reviewId") reviewId: Int,
        @Body storeReviewReportsRequest: StoreReviewReportsRequest
    ): Response<Unit?>

    @GET(URLConstant.OWNER.OWNER)
    suspend fun getOwnerTokenIsValid()

    @POST(URLConstant.ABTEST.ASSIGN.TOKEN)
    suspend fun updateABTestToken(): ABTestTokenResponse

    @POST(URLConstant.ABTEST.ASSIGN.ASSIGN)
    suspend fun postABTestAssign(
        @Body abTestRequest: ABTestRequest
    ): ABTestResponse

    @POST(URLConstant.SHOPS.SHOPID.NOTIFICATION)
    suspend fun postReviewPromptNotification(
        @Path("storeId") storeId: Int
    )
}
