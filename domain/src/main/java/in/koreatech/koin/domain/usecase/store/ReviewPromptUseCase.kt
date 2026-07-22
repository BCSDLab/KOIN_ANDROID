package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.repository.NotificationRepository
import javax.inject.Inject

class ReviewPromptUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(storeId: Int): Result<Unit> {
        return notificationRepository.postReviewPromptNotification(storeId)
    }
}
