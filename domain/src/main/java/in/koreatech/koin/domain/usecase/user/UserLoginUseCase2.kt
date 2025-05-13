package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.error.user.UserErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.TokenRepository
import `in`.koreatech.koin.domain.repository.UserRepository
import `in`.koreatech.koin.domain.util.ext.toSHA256
import javax.inject.Inject

class UserLoginUseCase2 @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    private val userErrorHandler: UserErrorHandler
) {
    suspend operator fun invoke(
        userId: String,
        password: String
    ): Pair<Unit?, ErrorHandler?> {
        return try {
            val authToken = userRepository.getToken(userId, password.toSHA256())
            if (authToken.userType == "STUDENT" || authToken.userType == "COUNCIL") {
                tokenRepository.saveAccessToken(authToken.token)
                tokenRepository.saveRefreshToken(authToken.refreshToken)
                userRepository.fetchStudentUserInfo(authToken.userType)
                Unit to null
            }
            else if(authToken.userType == "GENERAL") {
                tokenRepository.saveAccessToken(authToken.token)
                tokenRepository.saveRefreshToken(authToken.refreshToken)
                userRepository.fetchGeneralUserInfo(authToken.userType)
                Unit to null
            } else {
                Unit to null
            }
        }catch (throwable: Throwable) {
            null to userErrorHandler.handleGetTokenError2(throwable)
        }
    }
}
