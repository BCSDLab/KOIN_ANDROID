package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.request.owner.OwnerLoginRequest
import `in`.koreatech.koin.data.request.user.ABTestRequest
import `in`.koreatech.koin.data.request.user.IdRequest
import `in`.koreatech.koin.data.request.user.LoginRequest
import `in`.koreatech.koin.data.request.user.RefreshRequest
import `in`.koreatech.koin.data.request.user.StudentInfoRequest
import `in`.koreatech.koin.data.response.user.ABTestResponse
import `in`.koreatech.koin.data.response.owner.OwnerAuthResponse
import `in`.koreatech.koin.data.response.user.AuthResponse
import `in`.koreatech.koin.data.response.user.RefreshResponse
import retrofit2.Response
import retrofit2.http.*

interface UserApi {
    @POST(URLConstant.USER.LOGIN)
    suspend fun getToken(@Body loginRequest: LoginRequest): AuthResponse

    @POST(URLConstant.OWNER.SIGNIN)
    suspend fun getOwnerToken(@Body ownerLoginRequest: OwnerLoginRequest): OwnerAuthResponse

    @POST(URLConstant.USER.STUDENT.REGISTER)
    suspend fun postRegister(@Body studentInfoRequest: StudentInfoRequest)

    @POST(URLConstant.USER.FINDPASSWORD)
    suspend fun postPasswordReset(@Body idRequest: IdRequest)

    @GET(URLConstant.USER.CHECKNICKNAME)
    suspend fun checkNickname(@Query("nickname") nickname: String)

    @GET(URLConstant.USER.CHECKEMAIL)
    suspend fun checkEmail(@Query("address") email: String)

    @POST(URLConstant.USER.REFRESH)
    suspend fun postUserRefresh(@Body refreshRequest: RefreshRequest): Response<RefreshResponse>

    @POST("abtest/assign")
    suspend fun postABTestAssign(@Body abTestRequest: ABTestRequest): ABTestResponse
}