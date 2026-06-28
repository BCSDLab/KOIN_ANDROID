package `in`.koreatech.koin.domain.usecase.notification

import `in`.koreatech.koin.domain.model.notification.Notification
import `in`.koreatech.koin.domain.repository.NotificationRepository
import java.time.LocalDateTime
import javax.inject.Inject

class SaveNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(
        type: String,
        title: String,
        content: String,
        datetime: LocalDateTime = LocalDateTime.now()
    ): Result<Unit> {
        return notificationRepository.insertNotificationToLocal(
            notification = Notification(
                type = type,
                title = title,
                content = content,
                datetime = datetime
            )
        )
    }
}
