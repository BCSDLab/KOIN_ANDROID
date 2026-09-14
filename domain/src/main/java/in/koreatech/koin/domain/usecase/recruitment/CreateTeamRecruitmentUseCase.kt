package `in`.koreatech.koin.domain.usecase.recruitment

import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentRoleInput
import `in`.koreatech.koin.domain.repository.RecruitmentRepository
import javax.inject.Inject

class CreateTeamRecruitmentUseCase @Inject constructor(
    private val recruitmentRepository: RecruitmentRepository
) {
    suspend operator fun invoke(
        category: String,
        title: String,
        meetingType: String,
        activityStartDate: String,
        activityEndDate: String,
        deadlineDate: String,
        recruitmentType: String,
        maxParticipants: Int?,
        roles: List<TeamRecruitmentRoleInput>,
        description: String,
        relatedUrl: String?,
        qualification: String?
    ): Result<Int> =
        recruitmentRepository.createTeamRecruitment(
            category = category,
            title = title,
            meetingType = meetingType,
            activityStartDate = activityStartDate,
            activityEndDate = activityEndDate,
            deadlineDate = deadlineDate,
            recruitmentType = recruitmentType,
            maxParticipants = maxParticipants,
            roles = roles,
            description = description,
            relatedUrl = relatedUrl,
            qualification = qualification
        )
}
