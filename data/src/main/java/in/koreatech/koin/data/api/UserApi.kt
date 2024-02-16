package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.request.user.IdRequest
import `in`.koreatech.koin.data.request.user.LoginRequest
import `in`.koreatech.koin.data.request.user.RefreshRequest
import `in`.koreatech.koin.data.response.user.AuthResponse
import `in`.koreatech.koin.data.response.user.DefaultResponse
import `in`.koreatech.koin.data.response.user.RefreshResponse
import `in`.koreatech.koin.data.response.user.RegisterResponse
import retrofit2.Response
import `in`.koreatech.koin.data.request.user.StudentInfoRequest
import retrofit2.http.*

interface UserApi {
    @POST(URLConstant.USER.LOGIN)
    suspend fun getToken(@Body loginRequest: LoginRequest): AuthResponse

    @POST(URLConstant.USER.STUDENT.REGISTER)
    suspend fun postRegister(@Body studentInfoRequest: StudentInfoRequest)

    @POST(URLConstant.USER.FINDPASSWORD)
    suspend fun postPasswordReset(@Body idRequest: IdRequest): DefaultResponse

    @POST(URLConstant.USER.REFRESH)
    suspend fun postUserRefresh(@Body refreshRequest: RefreshRequest): Response<RefreshResponse>

    @GET(URLConstant.USER.CHECKNICKNAME)
    suspend fun checkNickname(@Query("nickname") nickname: String)

    @GET(URLConstant.USER.CHECKEMAIL)
    suspend fun checkEmail(@Query("address") email: String)
}