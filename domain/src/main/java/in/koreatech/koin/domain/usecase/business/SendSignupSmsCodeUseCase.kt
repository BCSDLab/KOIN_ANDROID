package `in`.koreatech.koin.domain.usecase.business

import `in`.koreatech.koin.domain.error.owner.OwnerErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.OwnerSignupRepository
import javax.inject.Inject

class SendSignupSmsCodeUseCase @Inject constructor(
    private val ownerSignupRepository: OwnerSignupRepository,
    private val ownerErrorHandler: OwnerErrorHandler,
) {
    suspend operator fun invoke(
        phoneNumber: String,
    ): ErrorHandler? {
        return try {
            ownerSignupRepository.requestSmsVerificationCode(phoneNumber)
            null
        } catch (t: Throwable) {
            ownerErrorHandler.handleSendSmsError(t)
        }
    }
}