package `in`.koreatech.koin.data.api

import OwnerRegisterRequest
import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.request.owner.OwnerChangePasswordRequest
import `in`.koreatech.koin.data.request.owner.OwnerEmailRegisterRequest
import `in`.koreatech.koin.data.request.owner.OwnerVerificationCodeRequest
import `in`.koreatech.koin.data.request.owner.OwnerVerificationEmailRequest
import `in`.koreatech.koin.data.request.owner.VerificationCodeSmsRequest
import `in`.koreatech.koin.data.request.owner.VerificationSmsRequest
import `in`.koreatech.koin.data.response.owner.OwnerResponse
import `in`.koreatech.koin.data.response.owner.OwnerVerificationCodeResponse
import `in`.koreatech.koin.data.response.store.StoreRegisterResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface OwnerApi {
    @POST(URLConstant.OWNER.CODE)
    suspend fun postVerificationCode(@Body ownerVerificationCode: OwnerVerificationCodeRequest): OwnerVerificationCodeResponse

    @POST(URLConstant.OWNER.EMAIL)
    suspend fun postVerificationEmail(@Body ownerVerificationEmail: OwnerVerificationEmailRequest)

    @POST(URLConstant.OWNER.REGISTER)
    suspend fun postOwnerEmailRegister(@Body ownerEmailRegisterRequest: OwnerEmailRegisterRequest): OwnerResponse

    @POST(URLConstant.OWNER.REGISTER_PHONE)
    suspend fun postOwnerRegister(@Body ownerRegisterRequest: OwnerRegisterRequest): OwnerResponse

    @POST(URLConstant.OWNER.CHANGEPASSWORDEMAIL)
    suspend fun changePasswordVerificationEmail(@Body ownerVerificationEmail: OwnerVerificationEmailRequest)

    @POST(URLConstant.OWNER.CHANGEPASSWORDCODE)
    suspend fun changePasswordVerificationCode(@Body ownerVerificationCode: OwnerVerificationCodeRequest)

    @PUT(URLConstant.OWNER.CHANGEPASSWORD)
    suspend fun changePassword(@Body ownerChangePasswordRequest: OwnerChangePasswordRequest)

    @GET(URLConstant.OWNER.EXISTS_ACCOUNT)
    suspend fun checkExistsAccount(@Query("account") account:String)

    @POST(URLConstant.OWNER.SMS)
    suspend fun postVerificationSms(@Body ownerVerificationSms: VerificationSmsRequest)

    @POST(URLConstant.OWNER.CODE_SMS)
    suspend fun postVerificationCodeSms(@Body ownerVerificationCode: VerificationCodeSmsRequest): OwnerVerificationCodeResponse

    @POST(URLConstant.OWNER.SHOPS)
    suspend fun putMyStore(@Body storeRegisterResponse: StoreRegisterResponse): StoreRegisterResponse
}