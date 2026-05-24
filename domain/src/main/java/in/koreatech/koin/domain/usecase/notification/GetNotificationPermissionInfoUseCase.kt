package `in`.koreatech.koin.domain.usecase.notification

import `in`.koreatech.koin.domain.model.notification.NotificationPermissionInfo
import `in`.koreatech.koin.domain.repository.NotificationRepository
import javax.inject.Inject

class GetNotificationPermissionInfoUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(): Result<NotificationPermissionInfo> {
        return notificationRepository.getPermissionInfo()
    }
}
