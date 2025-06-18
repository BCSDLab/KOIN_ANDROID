package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject

class CheckPhoneExistsUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(phone: String): Result<Unit> {
        return runCatching {
            userRepository.checkPhoneExists(phone)
        }
    }
}
