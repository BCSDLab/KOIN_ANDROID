package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject

class FindLoginIdByEmail @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, verificationCode: String): Result<String> {
        return userRepository.findLoginIdByEmail(email, verificationCode)
    }
}
