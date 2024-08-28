package `in`.koreatech.koin.domain.usecase.business.changepassword

import `in`.koreatech.koin.domain.error.owner.OwnerErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.OwnerChangePasswordRepository
import `in`.koreatech.koin.domain.state.business.changepw.ChangePasswordContinuationState
import `in`.koreatech.koin.domain.state.business.changepw.ChangePasswordExceptionState
import javax.inject.Inject

class SendAuthSmsCodeUseCase @Inject constructor(
    private val ownerChangePasswordRepository: OwnerChangePasswordRepository,
    private val ownerErrorHandler: OwnerErrorHandler,
) {
    suspend operator fun invoke(
        phoneNumber: String
    ): Pair<ChangePasswordContinuationState?, ErrorHandler?> {
        return try {
            ownerChangePasswordRepository.requestSmsVerification(
                phoneNumber = phoneNumber
            )
            ChangePasswordContinuationState.SendAuthCode to null
        } catch (t: Throwable) {
            null to ownerErrorHandler.handleFindPasswordError(t)
        }
    }
}