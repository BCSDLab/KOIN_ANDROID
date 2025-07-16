package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.error.user.UserErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
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
        email: String,
        password: String
    ): Pair<Unit?, ErrorHandler?> {
        return try {
            val authToken = userRepository.getToken(email, password.toSHA256())
            tokenRepository.saveAccessToken(authToken.token)
            tokenRepository.saveRefreshToken(authToken.refreshToken)
            when (UserType.valueOf(authToken.userType!!)) {
                UserType.COUNCIL, UserType.STUDENT -> {
                    userRepository.fetchStudentUserInfo()
                }

                UserType.GENERAL -> {
                    userRepository.fetchGeneralUserInfo()
                }

                UserType.ANONYMOUS -> {
                    // Do nothing
                }
            }
            Unit to null
        } catch (throwable: Throwable) {
            null to userErrorHandler.handleGetTokenError(throwable)
        }
    }
}
