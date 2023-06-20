package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.request.user.RefreshTokenRequest
import `in`.koreatech.koin.data.request.user.UserRequest
import `in`.koreatech.koin.data.response.AccessTokenResponse
import `in`.koreatech.koin.data.response.user.UserResponse
import `in`.koreatech.koin.data.response.user.DefaultResponse
import `in`.koreatech.koin.data.response.user.UserInfoEditResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserAuthApi {
    @GET(URLConstant.USER.STUDENT_ME)
    suspend fun getUser(): UserResponse

    @PUT(URLConstant.USER.STUDENT_ME)
    suspend fun putUser(@Body userRequest: UserRequest): UserResponse

    @DELETE(URLConstant.USER.USER)
    suspend fun deleteUser(): DefaultResponse

    @GET(URLConstant.USER.CHECKNICKNAME + "/{nickname}")
    suspend fun checkNickName(): UserInfoEditResponse
}