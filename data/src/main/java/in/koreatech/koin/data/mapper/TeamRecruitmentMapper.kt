package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.request.recruitment.TeamRecruitmentActivityRequest
import `in`.koreatech.koin.data.request.recruitment.TeamRecruitmentRoleRequest
import `in`.koreatech.koin.data.response.recruitment.TeamRecruitmentActivityResponse
import `in`.koreatech.koin.data.response.recruitment.TeamRecruitmentApplicationResponse
import `in`.koreatech.koin.data.response.recruitment.TeamRecruitmentApplicationRoleResponse
import `in`.koreatech.koin.data.response.recruitment.TeamRecruitmentProfileResponse
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentActivity
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentActivityInput
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentApplication
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentApplicationRole
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentProfile
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentRoleInput

fun TeamRecruitmentProfileResponse.toTeamRecruitmentProfile() = TeamRecruitmentProfile(
    profileNickname = profileNickname,
    department = department,
    major = major,
    studentNumber = studentNumber,
    preferredRole = preferredRole,
    skills = skills,
    activities = activities.map { it.toTeamRecruitmentActivity() },
    selfIntroduction = selfIntroduction
)

fun TeamRecruitmentActivityResponse.toTeamRecruitmentActivity() = TeamRecruitmentActivity(
    id = id,
    title = title,
    startedAt = startedAt,
    endedAt = endedAt,
    isOngoing = isOngoing,
    description = description
)

fun TeamRecruitmentActivityInput.toTeamRecruitmentActivityRequest() = TeamRecruitmentActivityRequest(
    title = title,
    startedAt = startedAt,
    endedAt = endedAt,
    isOngoing = isOngoing,
    description = description
)

fun TeamRecruitmentRoleInput.toTeamRecruitmentRoleRequest() = TeamRecruitmentRoleRequest(
    name = name,
    maxParticipants = maxParticipants
)

fun TeamRecruitmentApplicationResponse.toTeamRecruitmentApplication() = TeamRecruitmentApplication(
    applicationId = applicationId,
    recruitmentId = recruitmentId,
    status = status,
    role = role.toTeamRecruitmentApplicationRole(),
    createdAt = createdAt
)

fun TeamRecruitmentApplicationRoleResponse.toTeamRecruitmentApplicationRole() = TeamRecruitmentApplicationRole(
    id = id,
    name = name
)
