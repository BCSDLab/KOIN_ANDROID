package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.error.user.UserErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.TokenRepository
import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject

class ABTestUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    private val userErrorHandler: UserErrorHandler
) {
    suspend operator fun invoke(title: String): Pair<String?, ErrorHandler?> {
        return try {
            val accessHistory = userRepository.postABTestAssign(title)
            val accessHistoryId = accessHistory.accessHistoryId
            tokenRepository.saveAccessHistoryId(accessHistoryId)
            accessHistory.variableName to null
        } catch (t: Throwable) {
            null to userErrorHandler.handleVerifyUserPasswordError(t)
        }
    }
}