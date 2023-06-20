package `in`.koreatech.koin.domain.usecase.token

import `in`.koreatech.koin.domain.repository.TokenRepository
import javax.inject.Inject

class RefreshAccessTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(): String {
        val newAccessToken = tokenRepository.refreshAccessToken()

        tokenRepository.saveAccessToken(newAccessToken)

        return newAccessToken
    }
}