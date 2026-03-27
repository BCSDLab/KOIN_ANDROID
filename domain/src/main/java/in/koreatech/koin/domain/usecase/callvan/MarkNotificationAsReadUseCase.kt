package `in`.koreatech.koin.domain.usecase.callvan

import `in`.koreatech.koin.domain.repository.CallvanRepository
import javax.inject.Inject

class MarkNotificationAsReadUseCase @Inject constructor(
    private val callvanRepository: CallvanRepository
) {
    suspend operator fun invoke(
        notificationId: Int
    ): Result<Unit> = callvanRepository.markNotificationAsRead(
        notificationId = notificationId
    )
}
