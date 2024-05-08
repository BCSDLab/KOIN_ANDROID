package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.OwnerApi
import `in`.koreatech.koin.data.request.owner.OwnerChangePasswordRequest
import `in`.koreatech.koin.data.request.owner.OwnerRegisterRequest
import `in`.koreatech.koin.data.request.owner.OwnerVerificationCodeRequest
import `in`.koreatech.koin.data.request.owner.OwnerVerificationEmailRequest
import `in`.koreatech.koin.data.response.owner.OwnerResponse
import `in`.koreatech.koin.data.response.owner.OwnerVerificationCodeResponse

class OwnerRemoteDataSource(private val ownerApi: OwnerApi) {
    suspend fun postVerificationCode(ownerVerificationCode: OwnerVerificationCodeRequest): OwnerVerificationCodeResponse {
        return ownerApi.postVerificationCode(ownerVerificationCode)
    }

    suspend fun postVerificationEmail(ownerVerificationEmail: OwnerVerificationEmailRequest) {
        return ownerApi.postVerificationEmail(ownerVerificationEmail)
    }

    suspend fun changePasswordVerificationCode(ownerVerificationCode: OwnerVerificationCodeRequest) {
        return ownerApi.changePasswordVerificationCode(ownerVerificationCode)
    }

    suspend fun changePasswordVerificationEmail(ownerVerificationEmail: OwnerVerificationEmailRequest) {
        return ownerApi.changePasswordVerificationEmail(ownerVerificationEmail)
    }

    suspend fun postOwnerRegister(ownerRegisterRequest: OwnerRegisterRequest): OwnerResponse {
        return ownerApi.postOwnerRegister(ownerRegisterRequest)
    }

    suspend fun ownerChangePassword(ownerChangePasswordRequest: OwnerChangePasswordRequest) {
        return ownerApi.changePassword(ownerChangePasswordRequest)
    }
}