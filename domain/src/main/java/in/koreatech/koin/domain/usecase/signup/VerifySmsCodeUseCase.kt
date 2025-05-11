package `in`.koreatech.koin.domain.usecase.signup

import `in`.koreatech.koin.domain.repository.SignupRepository
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import javax.inject.Inject

class VerifySmsCodeUseCase @Inject constructor(
    private val signupRepository: SignupRepository
) {
    suspend operator fun invoke(
        phoneNumber: String,
        verificationCode: String
    ): SignupContinuationState {
        return signupRepository.verifyCertificationCode(phoneNumber, verificationCode)
    }
}
