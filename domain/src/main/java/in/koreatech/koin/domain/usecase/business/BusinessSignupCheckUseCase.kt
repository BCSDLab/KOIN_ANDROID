package `in`.koreatech.koin.domain.usecase.business

import `in`.koreatech.koin.domain.repository.OwnerVerificationCodeRepository
import `in`.koreatech.koin.domain.repository.TokenRepository
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.domain.util.ext.formatPhoneNumber
import `in`.koreatech.koin.domain.util.ext.isNotValidPassword
import javax.inject.Inject

class BusinessSignupCheckUseCase @Inject constructor(
    private val ownerVerificationCodeRepository: OwnerVerificationCodeRepository,
    private val tokenRepository: TokenRepository,
) {
    suspend operator fun invoke(
        password: String,
        passwordCheck: String,
        phoneNumber: String,
        verificationCode: String
    ): Result<SignupContinuationState> {
        return when {
            (password.isNotValidPassword()) -> Result.success(SignupContinuationState.PasswordIsNotValidate)
            (password != passwordCheck) -> Result.success(SignupContinuationState.PasswordNotMatching)
            (phoneNumber.length != 11) -> Result.success(SignupContinuationState.PhoneNumberIsNotValidate)
            else -> {
                val authToken = ownerVerificationCodeRepository.verifySmsCode(
                    phoneNumber.formatPhoneNumber(),
                    verificationCode
                )
                authToken.getOrDefault(defaultValue = null)
                    ?.let { tokenRepository.saveOwnerAccessToken(it.token) }
                if (authToken.isFailure) {
                    Result.failure(authToken.exceptionOrNull()!!)
                } else
                    Result.success(SignupContinuationState.CheckComplete)
            }
        }
    }
}
