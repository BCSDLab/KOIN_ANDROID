package `in`.koreatech.koin.domain.usecase.signup

import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject

class VerifyEmailCodeUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        email: String,
        verificationCode: String
    ): Result<Unit> {
        return userRepository.verifyEmailCode(email, verificationCode)
    }
}
