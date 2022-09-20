package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.error.token.TokenErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.TokenRepository
import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject

class UserLogoutUseCase @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val tokenErrorHandler: TokenErrorHandler
){
    suspend operator fun invoke() : ErrorHandler? {
        return try {
            tokenRepository.removeToken()
            null
        } catch (t: Throwable) {
            tokenErrorHandler.handleLogoutError(t)
        }
    }
}