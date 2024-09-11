package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.repository.UserRepository
import `in`.koreatech.koin.domain.util.ext.toSHA256
import javax.inject.Inject

class UpdateUserPasswordUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(password: String): Result<Unit> {
        return runCatching {
            userRepository.updateUserPassword(password.toSHA256())
        }
    }
}