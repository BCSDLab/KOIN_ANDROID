package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.request.owner.OwnerChangePasswordRequest
import `in`.koreatech.koin.data.request.owner.OwnerRegisterRequest
import `in`.koreatech.koin.data.request.owner.OwnerVerificationCodeRequest
import `in`.koreatech.koin.data.request.owner.OwnerVerificationEmailRequest
import `in`.koreatech.koin.data.request.owner.VerificationCodeSmsRequest
import `in`.koreatech.koin.data.request.owner.VerificationSmsRequest
import `in`.koreatech.koin.data.response.owner.OwnerResponse
import `in`.koreatech.koin.data.response.owner.OwnerVerificationCodeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface OwnerApi {
    @POST(URLConstant.OWNER.CODE)
    suspend fun postVerificationCode(@Body ownerVerificationCode: OwnerVerificationCodeRequest): OwnerVerificationCodeResponse

    @POST(URLConstant.OWNER.EMAIL)
    suspend fun postVerificationEmail(@Body ownerVerificationEmail: OwnerVerificationEmailRequest)

    @POST(URLConstant.OWNER.REGISTER)
    suspend fun postOwnerRegister(@Body ownerRegisterRequest: OwnerRegisterRequest): OwnerResponse

    @POST(URLConstant.OWNER.CHANGEPASSWORDEMAIL)
    suspend fun changePasswordVerificationEmail(@Body ownerVerificationEmail: OwnerVerificationEmailRequest)

    @POST(URLConstant.OWNER.CHANGEPASSWORDCODE)
    suspend fun changePasswordVerificationCode(@Body ownerVerificationCode: OwnerVerificationCodeRequest)

    @PUT(URLConstant.OWNER.CHANGEPASSWORD)
    suspend fun changePassword(@Body ownerChangePasswordRequest: OwnerChangePasswordRequest)
    @POST(URLConstant.OWNER.SMS)
    suspend fun postVerificationSms(@Body ownerVerificationSms: VerificationSmsRequest)

    @POST(URLConstant.OWNER.CODE_SMS)
    suspend fun postVerificationCodeSms(@Body ownerVerificationCode: VerificationCodeSmsRequest): OwnerVerificationCodeResponse
}