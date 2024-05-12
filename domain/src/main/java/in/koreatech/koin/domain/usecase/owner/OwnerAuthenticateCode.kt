package `in`.koreatech.koin.domain.usecase.owner

import `in`.koreatech.koin.domain.repository.OwnerChangePasswordRepository
import `in`.koreatech.koin.domain.state.business.changepw.ChangePasswordContinuationState
import `in`.koreatech.koin.domain.state.business.changepw.ChangePasswordExceptionState
import javax.inject.Inject

class OwnerAuthenticateCode @Inject constructor(
    private val ownerChangePasswordRepository: OwnerChangePasswordRepository
) {
    suspend operator fun invoke(
        email: String,
        authCode: String
    ): Result<ChangePasswordContinuationState> {
        return when (authCode) {
            "" -> Result.failure(ChangePasswordExceptionState.ToastNullAuthCode)
            else -> ownerChangePasswordRepository.authenticateCode(
                email = email,
                authCode = authCode
            ).map { ChangePasswordContinuationState.GotoChangePasswordScreen}
        }
    }

}