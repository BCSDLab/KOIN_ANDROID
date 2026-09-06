package `in`.koreatech.koin.domain.usecase.recruitment

import `in`.koreatech.koin.domain.model.recruitment.ApplicantDetail
import `in`.koreatech.koin.domain.repository.RecruitmentRepository
import javax.inject.Inject

class GetApplicantDetailUseCase @Inject constructor(
    private val recruitmentRepository: RecruitmentRepository
) {
    suspend operator fun invoke(recruitmentId: Int, applicationId: Int): Result<ApplicantDetail> =
        recruitmentRepository.getApplicantDetail(recruitmentId = recruitmentId, applicationId = applicationId)
}
