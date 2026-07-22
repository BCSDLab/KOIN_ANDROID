package `in`.koreatech.koin.domain.usecase.notification

import `in`.koreatech.koin.domain.model.notification.Notification
import `in`.koreatech.koin.domain.repository.NotificationRepository
import javax.inject.Inject

class UpdateNotificationReadByUrlUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(
        originUrl: String,
        isRead: Boolean
    ): Result<Notification> {
        return notificationRepository.updateNotificationReadByUrl(
            url = originUrl,
            isRead = isRead
        )
    }
}
