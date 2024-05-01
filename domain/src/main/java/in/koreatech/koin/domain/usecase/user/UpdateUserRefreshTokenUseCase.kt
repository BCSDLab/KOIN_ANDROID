package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.model.user.AuthToken
import `in`.koreatech.koin.domain.repository.TokenRepository
import javax.inject.Inject

class UpdateUserRefreshTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository,
) {
    suspend operator fun invoke(refreshBody: AuthToken) {
        tokenRepository.saveAccessToken(refreshBody.token)
        tokenRepository.saveRefreshToken(refreshBody.refreshToken)
    }
}