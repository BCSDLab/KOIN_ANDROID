package `in`.koreatech.koin.domain.usecase.recruitment

import `in`.koreatech.koin.domain.repository.RecruitmentRepository
import javax.inject.Inject

class UpdateApplicationStatusUseCase @Inject constructor(
    private val recruitmentRepository: RecruitmentRepository
) {
    suspend operator fun invoke(recruitmentId: Int, applicationId: Int, status: String): Result<Unit> =
        recruitmentRepository.updateApplicationStatus(
            recruitmentId = recruitmentId,
            applicationId = applicationId,
            status = status
        )
}
