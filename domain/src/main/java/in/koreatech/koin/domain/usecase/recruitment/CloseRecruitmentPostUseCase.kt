package `in`.koreatech.koin.domain.usecase.recruitment

import `in`.koreatech.koin.domain.repository.RecruitmentRepository
import javax.inject.Inject

class CloseRecruitmentPostUseCase @Inject constructor(
    private val recruitmentRepository: RecruitmentRepository
) {
    suspend operator fun invoke(postId: Int): Result<Unit> =
        recruitmentRepository.closeRecruitmentPost(postId)
}
