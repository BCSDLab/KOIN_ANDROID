package `in`.koreatech.koin.domain.usecase.recruitment

import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentActivityInput
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentProfile
import `in`.koreatech.koin.domain.model.user.StudentAcademicInfo
import `in`.koreatech.koin.domain.model.user.StudentAcademicInfoInput
import `in`.koreatech.koin.domain.repository.RecruitmentRepository
import `in`.koreatech.koin.domain.repository.UserRepository
import `in`.koreatech.koin.domain.util.suspendRunCatching
import javax.inject.Inject

class SaveTeamRecruitmentProfileUseCase @Inject constructor(
    private val recruitmentRepository: RecruitmentRepository,
    private val userRepository: UserRepository
) {
    @Suppress("LongParameterList")
    suspend operator fun invoke(
        profileNickname: String,
        preferredRole: String,
        skills: List<String>,
        activities: List<TeamRecruitmentActivityInput>,
        selfIntroduction: String,
        academicInfo: StudentAcademicInfoInput? = null,
        onAcademicInfoUpdated: suspend (StudentAcademicInfo) -> Unit = {}
    ): Result<TeamRecruitmentProfile> = suspendRunCatching {
        academicInfo?.let {
            val updatedAcademicInfo = userRepository.updateStudentAcademicInfo(
                department = it.department,
                major = it.major,
                studentNumber = it.studentNumber
            ).getOrThrow()
            onAcademicInfoUpdated(updatedAcademicInfo)
        }

        recruitmentRepository.saveTeamRecruitmentProfile(
            profileNickname = profileNickname,
            preferredRole = preferredRole,
            skills = skills,
            activities = activities,
            selfIntroduction = selfIntroduction
        ).getOrThrow()
    }
}
