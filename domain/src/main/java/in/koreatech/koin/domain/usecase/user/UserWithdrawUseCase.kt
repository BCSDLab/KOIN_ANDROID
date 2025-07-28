package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject

class UserWithdrawUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return runCatching {
            userRepository.deleteDeviceToken()
            userRepository.deleteUser()
        }
    }
}
