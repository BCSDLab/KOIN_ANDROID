package `in`.koreatech.koin.domain.usecase.notification

import `in`.koreatech.koin.domain.model.notification.Notification
import `in`.koreatech.koin.domain.repository.NotificationRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetNotificationsFlowUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    operator fun invoke(): Flow<List<Notification>> {
        return notificationRepository.getNotificationsFlowFromLocal()
    }
}
