package `in`.koreatech.koin.domain.usecase.business.changepw

import `in`.koreatech.koin.domain.repository.UploadUrlRepository
import `in`.koreatech.koin.domain.state.business.changepw.ChangePasswordContinuationState
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import javax.inject.Inject

class SendAuthCodeUseCase @Inject constructor(

) {
    suspend operator fun invoke(
        email: String
    ): Result<ChangePasswordContinuationState> {
        return when{
            email == "" -> Result.success(ChangePasswordContinuationState.ToastNoEmail)
            else -> {Result.success(ChangePasswordContinuationState.GotoChangePasswordScreen)}
        }
    }
}