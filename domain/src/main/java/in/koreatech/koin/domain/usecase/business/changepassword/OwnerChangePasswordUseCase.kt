package `in`.koreatech.koin.domain.usecase.business.changepassword

import `in`.koreatech.koin.domain.repository.OwnerChangePasswordRepository
import `in`.koreatech.koin.domain.state.business.changepw.ChangePasswordContinuationState
import `in`.koreatech.koin.domain.state.business.changepw.ChangePasswordExceptionState
import `in`.koreatech.koin.domain.util.ext.isNotValidPassword
import `in`.koreatech.koin.domain.util.ext.toSHA256
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class OwnerChangePasswordUseCase @Inject constructor(
private val ownerChangePasswordRepository: OwnerChangePasswordRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        passwordChanged: String
    ): Result<ChangePasswordContinuationState> {
        return try {
            when {
                email.isBlank() -> Result.failure(ChangePasswordExceptionState.ToastNullEmail)
                password.isBlank() -> Result.failure(ChangePasswordExceptionState.ToastNullPassword)
                password.isNotValidPassword() -> Result.failure(ChangePasswordExceptionState.ToastIsNotPasswordForm)
                passwordChanged.isBlank() -> Result.failure(ChangePasswordExceptionState.ToastNullPasswordChecked)
                password != passwordChanged -> Result.failure(ChangePasswordExceptionState.NotCoincidePassword)
                else -> ownerChangePasswordRepository.changePassword(
                    email = email,
                    password = password.toSHA256()
                ).map { ChangePasswordContinuationState.FinishedChangePassword}
            }
        }
        catch (t: CancellationException){
            throw t
        }
    }
}