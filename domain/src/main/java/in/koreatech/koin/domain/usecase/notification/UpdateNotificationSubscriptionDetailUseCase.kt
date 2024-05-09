package `in`.koreatech.koin.domain.usecase.notification

import `in`.koreatech.koin.domain.error.user.UserErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.model.notification.SubscribesDetailType
import `in`.koreatech.koin.domain.repository.NotificationRepository
import javax.inject.Inject

class UpdateNotificationSubscriptionDetailUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val userErrorHandler: UserErrorHandler,
) {
    suspend operator fun invoke(type: SubscribesDetailType): Pair<Unit?, ErrorHandler?> {
        return try {
            notificationRepository.updateSubscriptionDetail(type)
            Unit to null
        } catch (e: Exception) {
            null to userErrorHandler.handleUserError(e)
        }
    }
}