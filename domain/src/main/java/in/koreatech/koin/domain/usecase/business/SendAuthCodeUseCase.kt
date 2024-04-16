package `in`.koreatech.koin.domain.usecase.business

import `in`.koreatech.koin.domain.repository.OwnerSignupRepository
import `in`.koreatech.koin.domain.state.business.changepw.ChangePasswordContinuationState
import javax.inject.Inject

class SendAuthCodeUseCase @Inject constructor(
    private val ownerSignupRepository: OwnerSignupRepository
) {
    suspend operator fun invoke(
        email: String
    ): Result<ChangePasswordContinuationState> {
        return when (email) {
            "" -> Result.success(ChangePasswordContinuationState.ToastNullEmail)
            else -> ownerSignupRepository.requestEmailVerification(
                email = email
            ).map { ChangePasswordContinuationState.GotoChangePasswordScreen}
        }
    }
}