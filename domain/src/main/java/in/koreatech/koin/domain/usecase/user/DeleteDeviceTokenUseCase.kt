package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.error.user.UserErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject

class DeleteDeviceTokenUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val userErrorHandler: UserErrorHandler,
) {
    suspend operator fun invoke(): Pair<Unit?, ErrorHandler?> {
        return try {
            userRepository.deleteDeviceToken()
            Unit to null
        } catch (e: Exception) {
            null to userErrorHandler.handleUserError(e)
        }
    }
}