package `in`.koreatech.koin.domain.usecase.signup

import `in`.koreatech.koin.domain.model.user.PhoneNumber
import `in`.koreatech.koin.domain.repository.SignupRepository
import javax.inject.Inject

class RequestSmsVerificationUseCase @Inject constructor(
    private val signupRepository: SignupRepository
) {
    suspend operator fun invoke(phoneNumber: String): PhoneNumber {
        return signupRepository.requestSmsVerification(phoneNumber)
    }
}
