package `in`.koreatech.koin.domain.usecase.recruitment

import `in`.koreatech.koin.domain.repository.TeamRecruitmentRepository
import javax.inject.Inject

class CloseRecruitmentPostUseCase @Inject constructor(
    private val teamRecruitmentRepository: TeamRecruitmentRepository
) {
    suspend operator fun invoke(postId: Int): Result<Unit> =
        teamRecruitmentRepository.closeRecruitmentPost(postId)
}
