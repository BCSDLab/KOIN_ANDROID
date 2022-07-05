package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.repository.TokenRepository
import `in`.koreatech.koin.domain.repository.UserRepository
import `in`.koreatech.koin.domain.util.ext.toSHA256
import javax.inject.Inject

class UserLoginUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(
        portalAccount: String,
        password: String
    ): Result<Unit> {
        return try {
            val authToken = userRepository.getToken(portalAccount, password.toSHA256())
            tokenRepository.saveAccessToken(authToken.token)
            Result.success(Unit)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }
}