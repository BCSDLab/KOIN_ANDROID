package `in`.koreatech.koin.domain.usecase.notification

import `in`.koreatech.koin.domain.repository.NotificationRepository
import javax.inject.Inject

class DeleteNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(ids: List<Int>): Result<Unit> {
        return notificationRepository.deleteNotificationsFromLocal(ids)
    }
}
