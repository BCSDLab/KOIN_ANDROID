package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.error.user.UserErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.repository.TokenRepository
import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    private val userErrorHandler: UserErrorHandler
) {
    suspend operator fun invoke() : Pair<User?, ErrorHandler?> {
        return if(tokenRepository.getAccessToken() == null) {
            //익명
            null to null
        } else {
            try {
                userRepository.getUserInfo() to null
            } catch (t: Throwable) {
                null to userErrorHandler.handleGetUserInfoError(t)
            }
        }
    }
}