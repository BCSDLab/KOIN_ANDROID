package `in`.koreatech.koin.domain.usecase.signup

import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject

class VerifySmsCodeUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        phoneNumber: String,
        verificationCode: String
    ): Result<Unit> {
        return userRepository.verifyCertificationCode(phoneNumber, verificationCode)
    }
}
