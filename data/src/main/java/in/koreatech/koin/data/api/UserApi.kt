package `in`.koreatech.koin.data.api

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
import `in`.koreatech.koin.data.response.user.LoginIdResponse
import `in`.koreatech.koin.data.response.user.RefreshResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {
    @POST("v2/users/login")
    suspend fun getToken(
        @Body loginRequest: LoginRequest
    ): AuthResponse

    @POST("owner/login")
    suspend fun getOwnerToken(
        @Body ownerLoginRequest: OwnerLoginRequest
    ): OwnerAuthResponse

    @POST("user/student/register")
    suspend fun postRegister(
        @Body studentInfoRequest: StudentInfoRequest
    )

    @POST("user/find/password")
    suspend fun postPasswordReset(
        @Body idRequest: IdRequest
    )

    @GET("user/check/nickname")
    suspend fun checkNickname(
        @Query("nickname") nickname: String
    )

    @GET("user/check/email")
    suspend fun checkEmail(
        @Query("address") email: String
    )

    @POST("user/refresh")
    suspend fun postUserRefresh(
        @Body refreshRequest: RefreshRequest
    ): Response<RefreshResponse>

    @GET("user/check/phone")
    suspend fun checkPhoneNumberDuplicate(
        @Query("phone") phone: String
    )

    @GET("user/check/nickname")
    suspend fun checkNicknameV2(
        @Query("nickname") nickname: String
    )

    @GET("user/check/id")
    suspend fun checkLoginId(
        @Query("loginId") id: String
    )

    @POST("users/verification/sms/send")
    suspend fun smsSend(
        @Body smsSendRequest: SmsSendRequest
    ): CodeRequestCountResponse

    @POST("users/verification/email/send")
    suspend fun emailSend(
        @Body emailSendRequest: EmailSendRequest
    ): CodeRequestCountResponse

    @POST("v2/users/students/register")
    suspend fun postStudentRegister(
        @Body studentInfoRequest: StudentInfoRequestV2
    )

    @POST("v2/users/register")
    suspend fun postGeneralRegister(
        @Body generalInfoRequest: GeneralInfoRequest
    )

    @POST("users/verification/sms/verify")
    suspend fun codeVerify(
        @Body smsVerifyRequest: SmsVerifyRequest
    )

    @POST("users/verification/email/verify")
    suspend fun emailVerify(
        @Body emailVerifyRequest: EmailVerifyRequest
    )

    @POST("user/id/exists")
    suspend fun idExists(
        @Body idExistsRequest: IDExistsRequest
    )

    @POST("users/id/match/email")
    suspend fun idMatchEmail(
        @Body idMatchEmail: IdMatchEmail
    )

    @POST("users/id/match/phone")
    suspend fun idMatchPhone(
        @Body idMatchPhone: IdMatchPhone
    )

    @POST("users/password/reset/email")
    suspend fun passwordResetByEmail(
        @Body passwordResetByEmail: PasswordResetByEmail
    )

    @POST("users/password/reset/sms")
    suspend fun passwordResetBySms(
        @Body passwordResetBySms: PasswordResetBySms
    )

    @POST("user/email/exists")
    suspend fun checkEmailExists(
        @Body checkEmailExistsRequest: CheckEmailExistsRequest
    )

    @POST("user/phone/exists")
    suspend fun checkPhoneExists(
        @Body checkPhoneExistsRequest: CheckPhoneExistsRequest
    )

    @POST("users/id/find/email")
    suspend fun findIdByEmail(
        @Body emailVerifyRequest: EmailVerifyRequest // Find id use same DTO with email verification
    ): LoginIdResponse

    @POST("users/id/find/sms")
    suspend fun findIdBySms(
        @Body smsVerifyRequest: SmsVerifyRequest // Find id use same DTO with sms verification
    ): LoginIdResponse
}