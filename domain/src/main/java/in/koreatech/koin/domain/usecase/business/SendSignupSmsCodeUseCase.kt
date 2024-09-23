package `in`.koreatech.koin.domain.usecase.business

import `in`.koreatech.koin.domain.error.owner.OwnerErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.OwnerSignupRepository
import javax.inject.Inject

class SendSignupSmsCodeUseCase @Inject constructor(
    private val ownerSignupRepository: OwnerSignupRepository,
) {
    suspend operator fun invoke(
        phoneNumber: String,
    ): Result<Unit> {
        return try {
            ownerSignupRepository.requestSmsVerificationCode(phoneNumber)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }
}