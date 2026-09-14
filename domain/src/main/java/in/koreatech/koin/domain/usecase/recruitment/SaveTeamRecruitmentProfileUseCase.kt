package `in`.koreatech.koin.domain.usecase.recruitment

import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentActivityInput
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentProfile
import `in`.koreatech.koin.domain.repository.RecruitmentRepository
import javax.inject.Inject

class SaveTeamRecruitmentProfileUseCase @Inject constructor(
    private val recruitmentRepository: RecruitmentRepository
) {
    suspend operator fun invoke(
        profileNickname: String,
        preferredRole: String,
        skills: List<String>,
        activities: List<TeamRecruitmentActivityInput>,
        selfIntroduction: String
    ): Result<TeamRecruitmentProfile> =
        recruitmentRepository.saveTeamRecruitmentProfile(
            profileNickname = profileNickname,
            preferredRole = preferredRole,
            skills = skills,
            activities = activities,
            selfIntroduction = selfIntroduction
        )
}
