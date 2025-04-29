package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.request.owner.OwnerLoginRequest
import `in`.koreatech.koin.data.request.user.GeneralInfoRequest
import `in`.koreatech.koin.data.request.user.IdRequest
import `in`.koreatech.koin.data.request.user.LoginRequest
import `in`.koreatech.koin.data.request.user.RefreshRequest
import `in`.koreatech.koin.data.request.user.SmsSendRequest
import `in`.koreatech.koin.data.request.user.SmsVerifyRequest
import `in`.koreatech.koin.data.request.user.StudentInfoRequest
import `in`.koreatech.koin.data.request.user.StudentInfoRequestV2
import `in`.koreatech.koin.data.response.owner.OwnerAuthResponse
import `in`.koreatech.koin.data.response.user.AuthResponse
import `in`.koreatech.koin.data.response.user.CodeRequestCountResponse
import `in`.koreatech.koin.data.response.user.RefreshResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {
    @POST(URLConstant.USER.LOGIN)
    suspend fun getToken(
        @Body loginRequest: LoginRequest
    ): AuthResponse

    @POST(URLConstant.OWNER.SIGNIN)
    suspend fun getOwnerToken(
        @Body ownerLoginRequest: OwnerLoginRequest
    ): OwnerAuthResponse

    @POST(URLConstant.USER.STUDENT.REGISTER)
    suspend fun postRegister(
        @Body studentInfoRequest: StudentInfoRequest
    )

    @POST(URLConstant.USER.FINDPASSWORD)
    suspend fun postPasswordReset(
        @Body idRequest: IdRequest
    )

    @GET(URLConstant.USER.CHECKNICKNAME)
    suspend fun checkNickname(
        @Query("nickname") nickname: String
    )

    @GET(URLConstant.USER.CHECKEMAIL)
    suspend fun checkEmail(
        @Query("address") email: String
    )

    @POST(URLConstant.USER.REFRESH)
    suspend fun postUserRefresh(
        @Body refreshRequest: RefreshRequest
    ): Response<RefreshResponse>

    @GET(URLConstant.USER.CHECKPHONE)
    suspend fun checkPhoneNumberDuplicate(
        @Query("phone") phone: String
    )

    @GET(URLConstant.USER.CHECKNICKNAME_V2)
    suspend fun checkNicknameV2(
        @Query("nickname") nickname: String
    )

    @GET(URLConstant.USER.CHECKUSERID)
    suspend fun checkUserId(
        @Query("loginId") id: String
    )

    @POST(URLConstant.USER.SMSSEND)
    suspend fun smsSend(
        @Body smsSendRequest: SmsSendRequest
    ): CodeRequestCountResponse

    @POST(URLConstant.USER.STUDENT.REGISTER_V2)
    suspend fun postStudentRegister(
        @Body studentInfoRequest: StudentInfoRequestV2
    )

    @POST(URLConstant.USER.GENERAL.REGISTER)
    suspend fun postGeneralRegister(
        @Body generalInfoRequest: GeneralInfoRequest
    )

    @POST(URLConstant.USER.SMSVERIFY)
    suspend fun codeVerify(
        @Body smsVerifyRequest: SmsVerifyRequest
    )
}
