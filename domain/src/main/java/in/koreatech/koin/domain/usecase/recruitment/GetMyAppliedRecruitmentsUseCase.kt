package `in`.koreatech.koin.domain.usecase.recruitment

import `in`.koreatech.koin.domain.model.recruitment.MyAppliedRecruitments
import `in`.koreatech.koin.domain.repository.RecruitmentRepository
import javax.inject.Inject

class GetMyAppliedRecruitmentsUseCase @Inject constructor(
    private val recruitmentRepository: RecruitmentRepository
) {
    suspend operator fun invoke(
        statuses: List<String> = emptyList(),
        sort: String = "LATEST_DESC",
        page: Int = 1,
        limit: Int = 10
    ): Result<MyAppliedRecruitments> =
        recruitmentRepository.getMyAppliedRecruitments(statuses, sort, page, limit)
}
