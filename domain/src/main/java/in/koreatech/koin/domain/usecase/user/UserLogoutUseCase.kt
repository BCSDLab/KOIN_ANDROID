package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.error.token.TokenErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.TokenRepository
import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject
import `in`.koreatech.koin.domain.model.Result

class UserLogoutUseCase @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val tokenErrorHandler: TokenErrorHandler
){
    suspend operator fun invoke() : Result<Unit> {
        return try {
            tokenRepository.removeToken()
            Result.Success(Unit)
        } catch (t: Throwable) {
            tokenErrorHandler.handleLogoutError(t)
        }
    }
}