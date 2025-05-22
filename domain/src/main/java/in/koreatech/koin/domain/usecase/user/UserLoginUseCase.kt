package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.error.user.UserErrorHandler
import `in`.koreatech.koin.domain.model.user.UserType
import `in`.koreatech.koin.domain.repository.TokenRepository
import `in`.koreatech.koin.domain.repository.UserRepository
import `in`.koreatech.koin.domain.util.ext.toSHA256
import javax.inject.Inject

class UserLoginUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    private val userErrorHandler: UserErrorHandler
) {
    suspend operator fun invoke(
        loginId: String,
        password: String
    ): Result<Unit> {
        return try {
            val authToken = userRepository.getToken(loginId, password.toSHA256())
            when (authToken.userType) {
                UserType.STUDENT.name, UserType.COUNCIL.name -> {
                    tokenRepository.saveAccessToken(authToken.token)
                    tokenRepository.saveRefreshToken(authToken.refreshToken)
                    userRepository.fetchUserInfo(authToken.userType)
                    Result.success(Unit)
                }
                UserType.GENERAL.name -> {
                    tokenRepository.saveAccessToken(authToken.token)
                    tokenRepository.saveRefreshToken(authToken.refreshToken)
                    userRepository.fetchUserInfo(authToken.userType)
                    Result.success(Unit)
                }
                else -> {
                    Result.success(Unit)
                }
            }
        } catch (throwable: Throwable) {
            userErrorHandler.handleGetTokenErrorV2(throwable)
        }
    }
}
