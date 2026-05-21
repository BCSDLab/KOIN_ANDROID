package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.repository.UserRepository
import `in`.koreatech.koin.domain.util.suspendRunCatching
import javax.inject.Inject
import kotlin.Result

class UserWithdrawUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return suspendRunCatching {
            userRepository.deleteDeviceToken().getOrThrow()
            userRepository.deleteUser().getOrThrow()
        }
    }
}
