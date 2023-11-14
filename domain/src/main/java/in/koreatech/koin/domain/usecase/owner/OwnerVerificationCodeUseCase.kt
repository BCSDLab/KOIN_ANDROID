package `in`.koreatech.koin.domain.usecase.owner

import `in`.koreatech.koin.domain.repository.OwnerVerificationCodeRepository
import javax.inject.Inject

class OwnerVerificationCodeUseCase @Inject constructor(
    private val ownerVerificationCodeRepository: OwnerVerificationCodeRepository
) {
    suspend operator fun invoke(
        address: String,
        verificationCode: String
    ): Result<Unit> {
        return  ownerVerificationCodeRepository.compareVerificationCode(address, verificationCode)
    }
}