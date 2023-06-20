package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.error.user.UserErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.TokenRepository
import `in`.koreatech.koin.domain.repository.UserRepository
import `in`.koreatech.koin.domain.util.ext.toSHA256
import javax.inject.Inject

class UserLoginUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    private val userErrorHandler: UserErrorHandler,
) {
    suspend operator fun invoke(
        portalAccount: String,
        password: String
    ): Pair<Unit?, ErrorHandler?> {
        return try {
            val authToken = userRepository.getToken(portalAccount, password.toSHA256())
            tokenRepository.saveAccessToken(authToken.accessToken)
            if (authToken.refreshToken != null) tokenRepository.saveRefreshToken(authToken.refreshToken)
            Unit to null
        } catch (throwable: Throwable) {
            null to userErrorHandler.handleGetTokenError(throwable)
        }
    }
}