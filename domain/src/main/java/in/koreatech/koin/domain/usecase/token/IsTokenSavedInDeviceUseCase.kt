package `in`.koreatech.koin.domain.usecase.token

import `in`.koreatech.koin.domain.repository.TokenRepository
import javax.inject.Inject

class IsTokenSavedInDeviceUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke() : Boolean {
        return !tokenRepository.getAccessToken().isNullOrEmpty()
    }
}