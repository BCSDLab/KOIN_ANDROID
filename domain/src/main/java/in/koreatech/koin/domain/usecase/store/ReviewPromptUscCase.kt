package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.error.user.UserErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.NotificationRepository
import javax.inject.Inject

class ReviewPromptUscCase
    @Inject
    constructor(
        private val notificationRepository: NotificationRepository,
        private val userErrorHandler: UserErrorHandler,
    ) {
        suspend operator fun invoke(storeId: Int): Pair<Unit?, ErrorHandler?> {
            return try {
                notificationRepository.postReviewPromptNotification(storeId)
                Unit to null
            } catch (e: Exception) {
                null to userErrorHandler.handleUserError(e)
            }
        }
    }
