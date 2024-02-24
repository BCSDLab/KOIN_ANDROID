package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.request.user.IdRequest
import `in`.koreatech.koin.data.request.user.LoginRequest
import `in`.koreatech.koin.data.request.user.RefreshRequest
import `in`.koreatech.koin.data.response.user.AuthResponse
import `in`.koreatech.koin.data.response.user.CheckNicknameResponse
import `in`.koreatech.koin.data.response.user.DefaultResponse
import `in`.koreatech.koin.data.response.user.RefreshResponse
import `in`.koreatech.koin.data.response.user.RegisterResponse
import retrofit2.Response
import retrofit2.http.*

interface UserApi {
    @POST(URLConstant.USER.LOGIN)
    suspend fun getToken(@Body loginRequest: LoginRequest): AuthResponse

    @POST(URLConstant.USER.REGISTER)
    suspend fun postRegister(@Body loginRequest: LoginRequest): RegisterResponse

    @POST(URLConstant.USER.FINDPASSWORD)
    suspend fun postPasswordReset(@Body idRequest: IdRequest): DefaultResponse

    @GET(URLConstant.USER.CHECKNICKNAME + "/{nickname}")
    suspend fun checkNickname(@Path("nickname") nickname: String): CheckNicknameResponse

    @POST(URLConstant.USER.REFRESH)
    suspend fun postUserRefresh(@Body refreshRequest: RefreshRequest): Response<RefreshResponse>
}