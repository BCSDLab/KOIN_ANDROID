package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.error.token.TokenErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.TokenRepository
import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject

class UserLogoutUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenErrorHandler: TokenErrorHandler,
    private val deleteUserRefreshTokenUseCase: DeleteUserRefreshTokenUseCase
){
    suspend operator fun invoke() : Pair<Unit, ErrorHandler?> {
        return try {
            userRepository.deleteDeviceToken()
            deleteUserRefreshTokenUseCase()
            Unit to null
        } catch (t: Throwable) {
            Unit to tokenErrorHandler.handleLogoutError(t)
        }
    }
}