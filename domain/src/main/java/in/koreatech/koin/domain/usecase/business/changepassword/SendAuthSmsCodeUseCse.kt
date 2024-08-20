package `in`.koreatech.koin.domain.usecase.business.changepassword

import `in`.koreatech.koin.domain.repository.OwnerChangePasswordRepository
import `in`.koreatech.koin.domain.state.business.changepw.ChangePasswordContinuationState
import `in`.koreatech.koin.domain.state.business.changepw.ChangePasswordExceptionState
import `in`.koreatech.koin.domain.util.regex.isOwnerNotEmailValid
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class SendAuthSmsCodeUseCase @Inject constructor(
    private val ownerChangePasswordRepository: OwnerChangePasswordRepository
) {
    suspend operator fun invoke(
        phoneNumber: String
    ): Result<ChangePasswordContinuationState> {
        return try {
            when {
                phoneNumber == "" -> Result.failure(ChangePasswordExceptionState.ToastNullPhoneNumber)
                phoneNumber.length != 11 -> Result.failure(ChangePasswordExceptionState.ToastIsNotPhoneNumber)

                else -> ownerChangePasswordRepository.requestSmsVerification(
                    phoneNumber = phoneNumber
                ).map { ChangePasswordContinuationState.SendAuthCode}
            }
        }
        catch (t: CancellationException){
            throw t
        }
    }

}