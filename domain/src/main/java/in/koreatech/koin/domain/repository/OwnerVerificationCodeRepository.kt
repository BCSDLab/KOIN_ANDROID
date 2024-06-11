package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.owner.OwnerAuthToken


interface OwnerVerificationCodeRepository {
    suspend fun compareVerificationCode(
        address: String,
        verificationCode: String
    ): Result<OwnerAuthToken?>

    suspend fun verifySmsCode(
        phoneNumber: String,
        verificationCode: String
    ): Result<OwnerAuthToken?>
}