package `in`.koreatech.koin.domain.usecase.recruitment

import `in`.koreatech.koin.domain.model.recruitment.MyRecruitmentPost
import `in`.koreatech.koin.domain.repository.TeamRecruitmentRepository
import javax.inject.Inject

class GetMyRecruitmentPostsUseCase @Inject constructor(
    private val teamRecruitmentRepository: TeamRecruitmentRepository
) {
    suspend operator fun invoke(
        status: String = "ALL",
        sort: String = "LATEST_DESC",
        page: Int = 1,
        limit: Int = 20
    ): Result<List<MyRecruitmentPost>> =
        teamRecruitmentRepository.getMyRecruitmentPosts(status, sort, page, limit)
}
