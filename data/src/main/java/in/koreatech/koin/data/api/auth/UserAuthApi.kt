package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.request.user.PasswordRequest
import `in`.koreatech.koin.data.request.user.DeviceTokenRequest
import `in`.koreatech.koin.data.request.user.UserRequest
import `in`.koreatech.koin.data.response.notification.NotificationPermissionInfoResponse
import `in`.koreatech.koin.data.response.user.UserInfoEditResponse
import `in`.koreatech.koin.data.response.user.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface UserAuthApi {
    @GET(URLConstant.USER.ME)
    suspend fun getUser(): UserResponse

    @PUT(URLConstant.USER.ME)
    suspend fun putUser(@Body userRequest: UserRequest): UserResponse

    @DELETE(URLConstant.USER.USER)
    suspend fun deleteUser()

    @GET(URLConstant.USER.CHECKNICKNAME + "/{nickname}")
    suspend fun checkNickName(): UserInfoEditResponse

    @POST("/notification")
    suspend fun updateDeviceToken(@Body deviceTokenRequest: DeviceTokenRequest)

    @GET("/notification")
    suspend fun getNotificationPermissionInfo(): NotificationPermissionInfoResponse

    @POST("/notification/subscribe")
    suspend fun updateSubscription(@Query("type") type: String)

    @POST("/notification/subscribe/detail")
    suspend fun updateSubscriptionDetail(@Query("detail_type") type: String)

    @DELETE("/notification/subscribe")
    suspend fun deleteSubscription(@Query("type") type: String): Response<Unit?>

    @DELETE("/notification/subscribe/detail")
    suspend fun deleteSubscriptionDetail(@Query("detail_type") type: String): Response<Unit?>

    @DELETE("/notification")
    suspend fun deleteDeviceToken(): Response<Unit?>

    @POST(URLConstant.USER.CHECKPASSWORD)
    suspend fun checkPassword(@Body passwordRequest: PasswordRequest)
}