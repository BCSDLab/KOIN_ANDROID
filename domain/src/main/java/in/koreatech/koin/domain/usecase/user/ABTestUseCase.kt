package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.repository.TokenRepository
import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ABTestUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository
) {
    private val mutex = Mutex()

    suspend operator fun invoke(title: String): Result<String> {
        return runCatching {
            mutex.withLock {
                if (tokenRepository.getAccessHistoryId() == null) {
                    userRepository.updateABTestToken()
                }
            }
            userRepository.postABTestAssign(title).variableName
        }
    }
}
