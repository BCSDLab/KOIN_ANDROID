package `in`.koreatech.koin.domain.usecase.business.changepassword

import `in`.koreatech.koin.domain.error.owner.OwnerError
import `in`.koreatech.koin.domain.error.owner.OwnerErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.OwnerChangePasswordRepository
import `in`.koreatech.koin.domain.state.business.changepw.ChangePasswordContinuationState
import `in`.koreatech.koin.domain.state.business.changepw.ChangePasswordExceptionState
import javax.inject.Inject

class SendAuthSmsCodeUseCase @Inject constructor(
    private val ownerChangePasswordRepository: OwnerChangePasswordRepository,
) {
    suspend operator fun invoke(
        phoneNumber: String
    ): Result<ChangePasswordContinuationState> {
        return try {
            ownerChangePasswordRepository.requestSmsVerification(
                phoneNumber = phoneNumber
            )
            Result.success(ChangePasswordContinuationState.SendAuthCode)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }
}