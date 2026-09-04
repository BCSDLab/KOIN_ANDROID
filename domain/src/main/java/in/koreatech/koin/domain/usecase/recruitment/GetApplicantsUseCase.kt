package `in`.koreatech.koin.domain.usecase.recruitment

import `in`.koreatech.koin.domain.model.recruitment.ApplicantList
import `in`.koreatech.koin.domain.repository.RecruitmentRepository
import javax.inject.Inject

class GetApplicantsUseCase @Inject constructor(
    private val recruitmentRepository: RecruitmentRepository
) {
    suspend operator fun invoke(
        recruitmentId: Int,
        statuses: List<String>? = null,
        page: Int = 1,
        limit: Int = 20
    ): Result<ApplicantList> = recruitmentRepository.getApplicants(
        recruitmentId = recruitmentId,
        statuses = statuses,
        page = page,
        limit = limit
    )
}
