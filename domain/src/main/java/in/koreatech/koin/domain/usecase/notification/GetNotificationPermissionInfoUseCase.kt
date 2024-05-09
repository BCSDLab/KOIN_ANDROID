package `in`.koreatech.koin.domain.usecase.notification

import `in`.koreatech.koin.domain.error.user.UserErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.model.notification.NotificationPermissionInfo
import `in`.koreatech.koin.domain.repository.NotificationRepository
import javax.inject.Inject

class GetNotificationPermissionInfoUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val userErrorHandler: UserErrorHandler,
) {
    suspend operator fun invoke(): Pair<NotificationPermissionInfo?, ErrorHandler?> {
        return try {
            notificationRepository.getPermissionInfo() to null
        } catch (e: Exception) {
            null to userErrorHandler.handleUserError(e)
        }
    }
}