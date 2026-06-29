package `in`.koreatech.koin.domain.usecase.notification

import `in`.koreatech.koin.domain.model.notification.Notification
import `in`.koreatech.koin.domain.repository.NotificationRepository
import javax.inject.Inject

class UpdateNotificationsReadByIdUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(
        ids: List<Int>,
        isRead: Boolean
    ): Result<List<Notification>> {
        val results = mutableListOf<Notification>()
        ids.forEach { id ->
            notificationRepository.updateNotificationReadById(
                id = id,
                isRead = isRead
            ).fold(
                onSuccess = { results.add(it) },
                onFailure = { return Result.failure(it) }
            )
        }
        return Result.success(results)
    }
}
