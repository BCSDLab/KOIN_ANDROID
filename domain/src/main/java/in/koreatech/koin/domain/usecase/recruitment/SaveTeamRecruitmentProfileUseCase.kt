package `in`.koreatech.koin.domain.usecase.recruitment

import `in`.koreatech.koin.domain.error.recruitment.ProfileSaveFailedAfterAcademicUpdateException
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentActivityInput
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentProfile
import `in`.koreatech.koin.domain.model.user.StudentAcademicInfo
import `in`.koreatech.koin.domain.model.user.StudentAcademicInfoInput
import `in`.koreatech.koin.domain.repository.RecruitmentRepository
import `in`.koreatech.koin.domain.repository.UserRepository
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
        academicInfo: StudentAcademicInfoInput? = null
    ): Result<TeamRecruitmentProfile> {
        val updatedAcademicInfo: StudentAcademicInfo? = academicInfo?.let {
            userRepository.updateStudentAcademicInfo(
                department = it.department,
                major = it.major,
                studentNumber = it.studentNumber
            ).getOrElse { throwable -> return Result.failure(throwable) }
        }

        val result = recruitmentRepository.saveTeamRecruitmentProfile(
            profileNickname = profileNickname,
            preferredRole = preferredRole,
            skills = skills,
            activities = activities,
            selfIntroduction = selfIntroduction
        )

        if (updatedAcademicInfo == null) return result

        val profile = result.getOrElse { throwable ->
            return Result.failure(ProfileSaveFailedAfterAcademicUpdateException(updatedAcademicInfo, throwable))
        }
        return Result.success(profile)
    }
}
