package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.error.user.UserErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.repository.TokenRepository
import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject
import `in`.koreatech.koin.domain.model.Result
import `in`.koreatech.koin.domain.model.toResult

class GetUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    private val userErrorHandler: UserErrorHandler
) {
    suspend operator fun invoke(): Result<User?> {
        return if (tokenRepository.getAccessToken() == null) {
            //익명
            Result.Success<User?>(null)
        } else {
            try {
                userRepository.getUserInfo().toResult()
            } catch (t: Throwable) {
                userErrorHandler.handleGetUserInfoError(t)
            }
        }
    }
}