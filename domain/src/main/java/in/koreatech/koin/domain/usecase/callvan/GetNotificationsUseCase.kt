package `in`.koreatech.koin.domain.usecase.callvan

import `in`.koreatech.koin.domain.model.callvan.CallvanNotification
import `in`.koreatech.koin.domain.repository.CallvanRepository
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val callvanRepository: CallvanRepository
) {
    suspend operator fun invoke(): Result<List<CallvanNotification>> = callvanRepository.getNotifications()
}
