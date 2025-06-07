package `in`.koreatech.koin.domain.usecase.signup

import `in`.koreatech.koin.domain.model.user.VerificationCode
import `in`.koreatech.koin.domain.repository.SignupRepository
import javax.inject.Inject

class VerifySmsCodeUseCase @Inject constructor(
    private val signupRepository: SignupRepository
) {
    suspend operator fun invoke(
        phoneNumber: String,
        verificationCode: String
    ): VerificationCode {
        return signupRepository.verifyCertificationCode(phoneNumber, verificationCode)
    }
}
