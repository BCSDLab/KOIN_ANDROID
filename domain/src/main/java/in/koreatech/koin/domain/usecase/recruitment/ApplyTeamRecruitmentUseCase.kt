package `in`.koreatech.koin.domain.usecase.recruitment

import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentApplication
import `in`.koreatech.koin.domain.repository.RecruitmentRepository
import javax.inject.Inject

class ApplyTeamRecruitmentUseCase @Inject constructor(
    private val recruitmentRepository: RecruitmentRepository
) {
    suspend operator fun invoke(
        recruitmentId: Int,
        roleId: Int,
        motivation: String,
        availability: String
    ): Result<TeamRecruitmentApplication> =
        recruitmentRepository.applyTeamRecruitment(
            recruitmentId = recruitmentId,
            roleId = roleId,
            motivation = motivation,
            availability = availability
        )
}
