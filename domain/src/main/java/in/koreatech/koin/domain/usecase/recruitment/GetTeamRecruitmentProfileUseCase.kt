package `in`.koreatech.koin.domain.usecase.recruitment

import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentProfile
import `in`.koreatech.koin.domain.repository.RecruitmentRepository
import javax.inject.Inject

class GetTeamRecruitmentProfileUseCase @Inject constructor(
    private val recruitmentRepository: RecruitmentRepository
) {
    suspend operator fun invoke(): Result<TeamRecruitmentProfile> =
        recruitmentRepository.getTeamRecruitmentProfile()
}
