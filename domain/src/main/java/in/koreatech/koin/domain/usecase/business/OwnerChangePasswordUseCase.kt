package `in`.koreatech.koin.domain.usecase.business

import `in`.koreatech.koin.domain.repository.OwnerChangePasswordRepository
import `in`.koreatech.koin.domain.state.business.changepw.ChangePasswordContinuationState
import `in`.koreatech.koin.domain.state.business.changepw.ChangePasswordExceptionState
import `in`.koreatech.koin.domain.util.ext.isNotValidPassword
import javax.inject.Inject

class OwnerChangePasswordUseCase @Inject constructor(
private val ownerChangePasswordRepository: OwnerChangePasswordRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        passwordChanged: String
    ): Result<ChangePasswordContinuationState> {
        return when {
            email.isBlank() -> Result.failure(ChangePasswordExceptionState.ToastNullEmail)
            password.isBlank() -> Result.failure(ChangePasswordExceptionState.ToastNullPassword)
            password.isNotValidPassword() -> Result.failure(ChangePasswordExceptionState.ToastIsNotPasswordForm)
            passwordChanged.isBlank() -> Result.failure(ChangePasswordExceptionState.ToastNullPasswordChecked)
            password != passwordChanged -> Result.failure(ChangePasswordExceptionState.NotCoincidePassword)
            else -> ownerChangePasswordRepository.changePassword(
                email = email,
                password = password
            ).map { ChangePasswordContinuationState.FinishedChangePassword}
        }
    }

}