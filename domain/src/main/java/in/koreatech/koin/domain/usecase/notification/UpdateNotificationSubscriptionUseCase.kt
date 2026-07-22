package `in`.koreatech.koin.domain.usecase.notification

import `in`.koreatech.koin.domain.model.notification.SubscribesType
import `in`.koreatech.koin.domain.repository.NotificationRepository
import javax.inject.Inject

class UpdateNotificationSubscriptionUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(type: SubscribesType): Result<Unit> {
        return notificationRepository.updateSubscription(type)
    }
}
