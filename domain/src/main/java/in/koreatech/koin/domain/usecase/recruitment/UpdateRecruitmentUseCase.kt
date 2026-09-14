package `in`.koreatech.koin.domain.usecase.recruitment

import `in`.koreatech.koin.domain.model.recruitment.RecruitmentDetail
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentUpdate
import `in`.koreatech.koin.domain.repository.RecruitmentRepository
import javax.inject.Inject

class UpdateRecruitmentUseCase @Inject constructor(
    private val recruitmentRepository: RecruitmentRepository
) {
    suspend operator fun invoke(recruitmentId: Int, update: RecruitmentUpdate): Result<RecruitmentDetail> =
        recruitmentRepository.updateRecruitment(recruitmentId = recruitmentId, update = update)
}
