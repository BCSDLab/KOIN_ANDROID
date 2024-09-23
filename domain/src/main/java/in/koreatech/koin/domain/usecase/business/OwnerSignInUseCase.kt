package `in`.koreatech.koin.domain.usecase.business

import `in`.koreatech.koin.domain.error.user.UserErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.TokenRepository
import `in`.koreatech.koin.domain.repository.UserRepository
import `in`.koreatech.koin.domain.util.ext.toSHA256
import javax.inject.Inject

class OwnerSignInUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    private val userErrorHandler: UserErrorHandler,
) {
    suspend operator fun invoke(
        phoneNumber: String,
        password: String,
    ): Pair<Unit?, ErrorHandler?> {
        return try {
            val authToken = userRepository.getOwnerToken(phoneNumber, password.toSHA256())
            tokenRepository.saveAccessToken(authToken.token)
            tokenRepository.saveRefreshToken(authToken.refreshToken)
            Unit to null
        } catch (throwable: Throwable) {
            null to userErrorHandler.handleGetTokenError(throwable)
        }
    }
}