package `in`.koreatech.koin.domain.usecase.recruitment

import `in`.koreatech.koin.domain.model.recruitment.RecruitmentNotifications
import `in`.koreatech.koin.domain.repository.RecruitmentRepository
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val recruitmentRepository: RecruitmentRepository
) {
    suspend operator fun invoke(page: Int, limit: Int): Result<RecruitmentNotifications> =
        recruitmentRepository.getNotifications(page = page, limit = limit)
}
