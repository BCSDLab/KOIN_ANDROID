package `in`.koreatech.koin.domain.usecase.business.changepassword

import `in`.koreatech.koin.domain.repository.OwnerChangePasswordRepository
import `in`.koreatech.koin.domain.state.business.changepw.ChangePasswordContinuationState
import `in`.koreatech.koin.domain.state.business.changepw.ChangePasswordExceptionState
import javax.inject.Inject



class AuthenticateSmsCodeUseCase @Inject constructor(
    private val ownerChangePasswordRepository: OwnerChangePasswordRepository
) {
    suspend operator fun invoke(
        phoneNumber: String,
        authCode: String
    ): Result<ChangePasswordContinuationState> {
        return when (authCode) {
            "" -> Result.failure(ChangePasswordExceptionState.ToastNullAuthCode)
            else -> ownerChangePasswordRepository.authenticateSmsCode(
                phoneNumber = phoneNumber,
                authCode = authCode
            ).map { ChangePasswordContinuationState.GotoChangePasswordScreen}
        }
    }

}