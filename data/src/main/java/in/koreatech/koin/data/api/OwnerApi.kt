package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.request.owner.OwnerSignUpRequest
import `in`.koreatech.koin.data.request.owner.OwnerVerificationCodeRequest
import `in`.koreatech.koin.data.request.owner.OwnerVerificationEmailRequest
import `in`.koreatech.koin.data.response.owner.OwnerResponse
import `in`.koreatech.koin.data.response.owner.OwnerVerificationCodeResponse
import `in`.koreatech.koin.data.response.user.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface OwnerApi {
    @POST(URLConstant.OWNER.REGISTER)
    suspend fun postRegister(@Body ownerLoginRequest: OwnerSignUpRequest): RegisterResponse

//    @POST(URLConstant.OWNER.CODE)
//    suspend fun postVerificationCode(@Body ownerVerificationCode: OwnerVerificationCodeRequest): OwnerResponse

    @POST(URLConstant.OWNER.CODE)
    suspend fun postVerificationCode(@Body ownerVerificationCode: OwnerVerificationCodeRequest): OwnerVerificationCodeResponse

    @POST(URLConstant.OWNER.EMAIL)
    suspend fun postVerificationEmail(@Body ownerVerificationEmail: OwnerVerificationEmailRequest): Response<Unit>
}