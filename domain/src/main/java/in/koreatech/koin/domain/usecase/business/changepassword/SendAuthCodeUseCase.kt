package `in`.koreatech.koin.domain.usecase.business.changepassword

import `in`.koreatech.koin.domain.repository.OwnerChangePasswordRepository
import `in`.koreatech.koin.domain.state.business.changepw.ChangePasswordContinuationState
import `in`.koreatech.koin.domain.state.business.changepw.ChangePasswordExceptionState
import `in`.koreatech.koin.domain.util.regex.isOwnerNotEmailValid
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class SendAuthCodeUseCase @Inject constructor(
    private val ownerChangePasswordRepository: OwnerChangePasswordRepository
) {
    suspend operator fun invoke(
        email: String
    ): Result<ChangePasswordContinuationState> {
        return try {
            when {
                email == "" -> Result.failure(ChangePasswordExceptionState.ToastNullEmail)
                email.isOwnerNotEmailValid() -> Result.failure(ChangePasswordExceptionState.ToastIsNotEmail)
                else -> ownerChangePasswordRepository.requestEmailVerification(
                    email = email
                ).map { ChangePasswordContinuationState.SendAuthCode}
            }
        }
        catch (t: CancellationException){
            throw t
        }
    }

}