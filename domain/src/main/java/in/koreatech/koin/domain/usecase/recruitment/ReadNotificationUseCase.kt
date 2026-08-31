package `in`.koreatech.koin.domain.usecase.recruitment

import `in`.koreatech.koin.domain.repository.RecruitmentRepository
import javax.inject.Inject

class ReadNotificationUseCase @Inject constructor(
    private val recruitmentRepository: RecruitmentRepository
) {
    suspend operator fun invoke(notificationId: Int): Result<Unit> =
        recruitmentRepository.readNotification(notificationId = notificationId)
}
