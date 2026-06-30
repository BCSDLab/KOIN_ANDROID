package `in`.koreatech.koin.domain.usecase.notification

import `in`.koreatech.koin.domain.model.notification.Notification
import `in`.koreatech.koin.domain.repository.NotificationRepository
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class UpdateNotificationsReadByIdUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(
        ids: List<Int>,
        isRead: Boolean
    ): Result<List<Notification>> = coroutineScope {
        val results = ids.map { id ->
            async {
                notificationRepository.updateNotificationReadById(
                    id = id,
                    isRead = isRead
                )
            }
        }.awaitAll()

        Result.success(results.mapNotNull { it.getOrNull() })
    }
}
