package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.request.owner.OwnerLoginRequest
import `in`.koreatech.koin.data.request.user.CheckEmailExistsRequest
import `in`.koreatech.koin.data.request.user.CheckPhoneExistsRequest
import `in`.koreatech.koin.data.request.user.EmailSendRequest
import `in`.koreatech.koin.data.request.user.EmailVerifyRequest
import `in`.koreatech.koin.data.request.user.GeneralInfoRequest
import `in`.koreatech.koin.data.request.user.IdRequest
import `in`.koreatech.koin.data.request.user.LoginRequest
import `in`.koreatech.koin.data.request.user.RefreshRequest
import `in`.koreatech.koin.data.request.user.SmsSendRequest
import `in`.koreatech.koin.data.request.user.SmsVerifyRequest
import `in`.koreatech.koin.data.request.user.StudentInfoRequest
import `in`.koreatech.koin.data.request.user.StudentInfoRequestV2
import `in`.koreatech.koin.data.request.user.findpassword.IDExistsRequest
import `in`.koreatech.koin.data.request.user.findpassword.IdMatchEmail
import `in`.koreatech.koin.data.request.user.findpassword.IdMatchPhone
import `in`.koreatech.koin.data.request.user.findpassword.PasswordResetByEmail
import `in`.koreatech.koin.data.request.user.findpassword.PasswordResetBySms
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
    @POST(URLConstant.USERS.SIGNIN_V2)
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
    suspend fun checkLoginId(
        @Query("loginId") id: String
    )

    @POST(URLConstant.USERS.SMSSEND)
    suspend fun smsSend(
        @Body smsSendRequest: SmsSendRequest
    ): CodeRequestCountResponse

    @POST(URLConstant.USERS.EMAILSEND)
    suspend fun emailSend(
        @Body emailSendRequest: EmailSendRequest
    ): CodeRequestCountResponse

    @POST(URLConstant.USERS.STUDENTS.REGISTER_V2)
    suspend fun postStudentRegister(
        @Body studentInfoRequest: StudentInfoRequestV2
    )

    @POST(URLConstant.USERS.GENERAL.REGISTER)
    suspend fun postGeneralRegister(
        @Body generalInfoRequest: GeneralInfoRequest
    )

    @POST(URLConstant.USERS.SMSVERIFY)
    suspend fun codeVerify(
        @Body smsVerifyRequest: SmsVerifyRequest
    )

    @POST(URLConstant.USERS.EMAILVERIFY)
    suspend fun emailVerify(
        @Body emailVerifyRequest: EmailVerifyRequest
    )

    @POST(URLConstant.USER.ID_EXISTS)
    suspend fun idExists(
        @Body idExistsRequest: IDExistsRequest
    )

    @POST(URLConstant.USERS.ID_MATCH_EMAIL)
    suspend fun idMatchEmail(
        @Body idMatchEmail: IdMatchEmail
    )

    @POST(URLConstant.USERS.ID_MATCH_PHONE)
    suspend fun idMatchPhone(
        @Body idMatchPhone: IdMatchPhone
    )

    @POST(URLConstant.USERS.PASSWORD_RESET_BY_EMAIL)
    suspend fun passwordResetByEmail(
        @Body passwordResetByEmail: PasswordResetByEmail
    )

    @POST(URLConstant.USERS.PASSWORD_RESET_BY_SMS)
    suspend fun passwordResetBySms(
        @Body passwordResetBySms: PasswordResetBySms
    )

    @POST(URLConstant.USER.EXISTS.EMAIL)
    suspend fun checkEmailExists(
        @Body checkEmailExistsRequest: CheckEmailExistsRequest
    )

    @POST(URLConstant.USER.EXISTS.PHONE)
    suspend fun checkPhoneExists(
        @Body checkPhoneExistsRequest: CheckPhoneExistsRequest
    )
}
