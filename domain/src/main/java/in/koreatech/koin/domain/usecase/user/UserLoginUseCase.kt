package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.model.user.UserType
import `in`.koreatech.koin.domain.repository.TokenRepository
import `in`.koreatech.koin.domain.repository.UserRepository
import `in`.koreatech.koin.domain.util.ext.toSHA256
import `in`.koreatech.koin.domain.util.suspendRunCatching
import javax.inject.Inject
import kotlin.Result

class UserLoginUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<Unit> {
        return suspendRunCatching {
            val authToken = userRepository.getToken(email, password.toSHA256()).getOrThrow()
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
        }
    }
}
