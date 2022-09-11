package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.repository.TokenRepository
import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
){
    suspend operator fun invoke() {
        tokenRepository.removeToken()
    }
}