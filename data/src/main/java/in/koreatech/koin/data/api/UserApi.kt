package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.request.user.IdRequest
import `in`.koreatech.koin.data.request.user.LoginRequest
import `in`.koreatech.koin.data.response.AuthResponse
import `in`.koreatech.koin.data.response.DefaultResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST(URLConstant.USER.LOGIN)
    suspend fun getToken(@Body loginRequest: LoginRequest): AuthResponse

    @POST(URLConstant.USER.REGISTER)
    suspend fun postRegister(@Body loginRequest: LoginRequest): DefaultResponse

    @POST(URLConstant.USER.FINDPASSWORD)
    suspend fun postPasswordReset(@Body idRequest: IdRequest): DefaultResponse
}