package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.OwnerApi
import `in`.koreatech.koin.data.request.owner.OwnerRequest
import `in`.koreatech.koin.data.request.owner.OwnerVerificationCodeRequest
import `in`.koreatech.koin.data.request.owner.OwnerVerificationEmailRequest
import `in`.koreatech.koin.data.response.owner.OwnerResponse

class OwnerRemoteDataSource(private val ownerApi: OwnerApi) {
    suspend fun postRegister(ownerRequest: OwnerRequest): OwnerResponse {
        return ownerApi.postRegister(ownerRequest)
    }

    suspend fun postVerificationCode(ownerVerificationCode: OwnerVerificationCodeRequest): OwnerResponse {
        return ownerApi.postVerificationCode(ownerVerificationCode)
    }

    suspend fun postVerificationEmail(ownerVerificationEmail: OwnerVerificationEmailRequest): OwnerResponse {
        return ownerApi.postVerificationEmail(ownerVerificationEmail)
    }
}