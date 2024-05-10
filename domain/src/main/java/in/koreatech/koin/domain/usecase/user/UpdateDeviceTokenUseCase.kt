package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.error.user.UserErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject

class UpdateDeviceTokenUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val userErrorHandler: UserErrorHandler
) {
    suspend operator fun invoke(token: String): Pair<Unit?, ErrorHandler?> =
        try {
            userRepository.updateDeviceToken(token)
            Unit to null
        } catch (throwable: Throwable) {
            null to userErrorHandler.handleUserError(throwable)
        }
}