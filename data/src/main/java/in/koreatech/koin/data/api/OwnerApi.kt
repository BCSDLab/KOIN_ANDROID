package `in`.koreatech.koin.data.api

import OwnerRegisterRequest
import `in`.koreatech.koin.data.request.owner.OwnerChangePasswordRequest
import `in`.koreatech.koin.data.request.owner.OwnerChangePasswordSmsRequest
import `in`.koreatech.koin.data.request.owner.OwnerEmailRegisterRequest
import `in`.koreatech.koin.data.request.owner.OwnerVerificationCodeRequest
import `in`.koreatech.koin.data.request.owner.OwnerVerificationEmailRequest
import `in`.koreatech.koin.data.request.owner.VerificationCodeSmsRequest
import `in`.koreatech.koin.data.request.owner.VerificationSmsRequest
import `in`.koreatech.koin.data.response.owner.CheckCompanyNumberResponse
import `in`.koreatech.koin.data.response.owner.OwnerResponse
import `in`.koreatech.koin.data.response.owner.OwnerVerificationCodeResponse
import `in`.koreatech.koin.data.response.store.StoreRegisterResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface OwnerApi {
    @POST("owners/verification/code")
    suspend fun postVerificationCode(
        @Body ownerVerificationCode: OwnerVerificationCodeRequest
    ): OwnerVerificationCodeResponse

    @POST("owners/verification/email")
    suspend fun postVerificationEmail(
        @Body ownerVerificationEmail: OwnerVerificationEmailRequest
    )

    @POST("owners/register")
    suspend fun postOwnerEmailRegister(
        @Body ownerEmailRegisterRequest: OwnerEmailRegisterRequest
    ): OwnerResponse

    @POST("owners/register/phone")
    suspend fun postOwnerRegister(
        @Body ownerRegisterRequest: OwnerRegisterRequest
    )

    // 비밀번호 변경 인증번호 발송
    @POST("owners/password/reset/verification")
    suspend fun changePasswordVerificationEmail(
        @Body ownerVerificationEmail: OwnerVerificationEmailRequest
    )

    @POST("owners/password/reset/verification/sms")
    suspend fun changePasswordVerificationSms(
        @Body ownerVerificationSms: VerificationSmsRequest
    )

    // 비밀번호 변경 인증번호 확인
    @POST("owners/password/reset/send")
    suspend fun changePasswordVerificationCode(
        @Body ownerVerificationCode: OwnerVerificationCodeRequest
    )

    @POST("owners/password/reset/send/sms")
    suspend fun changePasswordVerificationCode(
        @Body ownerVerificationSmsCode: VerificationCodeSmsRequest
    )

    // 비밀번호 변경
    @PUT("owners/password/reset")
    suspend fun changePassword(
        @Body ownerChangePasswordRequest: OwnerChangePasswordRequest
    )

    @PUT("owners/password/reset/sms")
    suspend fun changePasswordSms(
        @Body ownerChangePasswordSmsRequest: OwnerChangePasswordSmsRequest
    )

    @GET("owners/exists/account")
    suspend fun checkExistsAccount(
        @Query("account") account: String
    )

    @POST("owners/verification/sms")
    suspend fun postVerificationSms(
        @Body ownerVerificationSms: VerificationSmsRequest
    )

    @POST("owners/verification/code/sms")
    suspend fun postVerificationCodeSms(
        @Body ownerVerificationCode: VerificationCodeSmsRequest
    ): OwnerVerificationCodeResponse

    @POST("owner/shops")
    suspend fun putMyStore(
        @Body storeRegisterResponse: StoreRegisterResponse
    ): StoreRegisterResponse

    @POST("owners/exists/company-number")
    suspend fun checkExistsCompanyNumber(
        @Body companyNumber: CheckCompanyNumberResponse
    )
}
