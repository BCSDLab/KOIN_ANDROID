package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.repository.TokenRepository
import javax.inject.Inject

class DeleteUserRefreshTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository,
) {
    suspend operator fun invoke() {
        tokenRepository.removeToken()
        tokenRepository.removeRefreshToken()
    }
}
