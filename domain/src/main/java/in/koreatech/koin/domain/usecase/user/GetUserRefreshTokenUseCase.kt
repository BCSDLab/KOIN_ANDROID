package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.model.user.AuthToken
import `in`.koreatech.koin.domain.repository.TokenRepository
import javax.inject.Inject

class GetUserRefreshTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository,
) {
    suspend operator fun invoke(isResponseSuccess: Boolean, refreshBody: AuthToken?): String? = runCatching {
        if (isResponseSuccess &&  refreshBody != null) {
            tokenRepository.saveAccessToken(refreshBody.token)
            tokenRepository.saveRefreshToken(refreshBody.refreshToken)
            refreshBody.token
        } else {
            tokenRepository.removeToken()
            tokenRepository.removeRefreshToken()
            null
        }
    }.getOrNull()
}