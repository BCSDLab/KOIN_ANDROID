package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.OwnerApi
import `in`.koreatech.koin.data.request.owner.OwnerLoginRequest
import `in`.koreatech.koin.data.request.owner.OwnerVerificationCodeRequest
import `in`.koreatech.koin.data.request.owner.OwnerVerificationEmailRequest
import `in`.koreatech.koin.data.response.owner.OwnerResponse
import `in`.koreatech.koin.data.response.user.RegisterResponse

class OwnerRemoteDataSource(private val ownerApi: OwnerApi) {

    suspend fun postVerificationCode(ownerVerificationCode: OwnerVerificationCodeRequest): OwnerResponse {
        return ownerApi.postVerificationCode(ownerVerificationCode)
    }

    suspend fun postVerificationEmail(ownerVerificationEmail: OwnerVerificationEmailRequest): OwnerResponse {
        return ownerApi.postVerificationEmail(ownerVerificationEmail)
    }

    suspend fun sendRegisterEmail(
        ownerLoginRequest: OwnerLoginRequest
    ): RegisterResponse {
        return ownerApi.postRegister(ownerLoginRequest)
    }
}