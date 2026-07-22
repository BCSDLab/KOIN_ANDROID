package `in`.koreatech.koin.domain.usecase.notification

import `in`.koreatech.koin.domain.model.notification.SubscribesDetailType
import `in`.koreatech.koin.domain.repository.NotificationRepository
import javax.inject.Inject

class DeleteNotificationSubscriptionDetailUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(type: SubscribesDetailType): Result<Unit> {
        return notificationRepository.deleteSubscriptionDetail(type)
    }
}
