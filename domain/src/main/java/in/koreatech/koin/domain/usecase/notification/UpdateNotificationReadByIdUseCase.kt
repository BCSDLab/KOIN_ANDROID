package `in`.koreatech.koin.domain.usecase.notification

import `in`.koreatech.koin.domain.model.notification.Notification
import `in`.koreatech.koin.domain.repository.NotificationRepository
import javax.inject.Inject

class UpdateNotificationReadByIdUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(
        id: Int,
        isRead: Boolean
    ): Result<Notification> {
        return notificationRepository.updateNotificationReadById(
            id = id,
            isRead = isRead
        )
    }
}
