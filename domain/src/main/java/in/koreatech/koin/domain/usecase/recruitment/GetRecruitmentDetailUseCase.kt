package `in`.koreatech.koin.domain.usecase.recruitment

import `in`.koreatech.koin.domain.model.recruitment.RecruitmentDetail
import `in`.koreatech.koin.domain.repository.RecruitmentRepository
import javax.inject.Inject

class GetRecruitmentDetailUseCase @Inject constructor(
    private val recruitmentRepository: RecruitmentRepository
) {
    suspend operator fun invoke(recruitmentId: Int): Result<RecruitmentDetail> =
        recruitmentRepository.getRecruitmentDetail(recruitmentId = recruitmentId)
}
