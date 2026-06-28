package `in`.koreatech.koin.domain.usecase.notification

import `in`.koreatech.koin.domain.repository.NotificationRepository
import javax.inject.Inject

class DeleteNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(id: Int): Result<Unit> {
        return notificationRepository.deleteNotificationFromLocal(id)
    }
}
