package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject

class CheckIdExistsUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(loginId: String): Result<Unit> {
        return runCatching {
            userRepository.checkIdExists(loginId)
        }
    }
}
