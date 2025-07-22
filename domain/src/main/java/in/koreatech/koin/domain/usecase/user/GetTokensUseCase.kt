package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.model.user.AuthToken
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.model.user.UserType
import `in`.koreatech.koin.domain.repository.TokenRepository
import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject

class GetTokensUseCase @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val userRepository: UserRepository
    ) {
    suspend operator fun invoke(): AuthToken {
        val accessToken = tokenRepository.getAccessToken() ?: ""
        val refreshToken = tokenRepository.getRefreshToken() ?: ""
        val userType = when (userRepository.getUserInfo()) {
            User.Anonymous -> UserType.ANONYMOUS.name
            is User.Student -> UserType.STUDENT.name
            is User.General -> UserType.GENERAL.name
        }

        return AuthToken(
            token = accessToken,
            refreshToken = refreshToken,
            userType = userType
        )
    }
}