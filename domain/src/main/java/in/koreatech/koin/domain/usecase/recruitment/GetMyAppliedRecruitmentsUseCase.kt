package `in`.koreatech.koin.domain.usecase.recruitment

import `in`.koreatech.koin.domain.model.recruitment.MyAppliedRecruitment
import `in`.koreatech.koin.domain.repository.TeamRecruitmentRepository
import javax.inject.Inject

class GetMyAppliedRecruitmentsUseCase @Inject constructor(
    private val teamRecruitmentRepository: TeamRecruitmentRepository
) {
    suspend operator fun invoke(
        statuses: List<String> = emptyList(),
        sort: String = "LATEST_DESC",
        page: Int = 1,
        limit: Int = 10
    ): Result<List<MyAppliedRecruitment>> =
        teamRecruitmentRepository.getMyAppliedRecruitments(statuses, sort, page, limit)
}
