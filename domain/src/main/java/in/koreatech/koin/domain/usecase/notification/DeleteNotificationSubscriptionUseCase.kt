package `in`.koreatech.koin.domain.usecase.notification

import `in`.koreatech.koin.domain.error.user.UserErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.model.notification.SubscribesType
import `in`.koreatech.koin.domain.repository.NotificationRepository
import javax.inject.Inject

class DeleteNotificationSubscriptionUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val userErrorHandler: UserErrorHandler,
) {
    suspend operator fun invoke(type: SubscribesType): Pair<Unit?, ErrorHandler?> {
        return try {
            notificationRepository.deleteSubscription(type)
            Unit to null
        } catch (e: Exception) {
            null to userErrorHandler.handleUserError(e)
        }
    }
}