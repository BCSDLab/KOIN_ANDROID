package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject

class CheckIdMatchEmailUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(loginId: String, email: String): Result<Unit> {
        return userRepository.checkIdMatchEmail(loginId, email)
    }
}
