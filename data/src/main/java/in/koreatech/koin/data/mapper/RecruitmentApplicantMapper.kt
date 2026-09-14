package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.response.recruitment.ApplicantDetailResponse
import `in`.koreatech.koin.data.response.recruitment.ApplicantListResponse
import `in`.koreatech.koin.data.response.recruitment.ApplicantRecruitmentResponse
import `in`.koreatech.koin.data.response.recruitment.ApplicantSummaryResponse
import `in`.koreatech.koin.data.response.recruitment.ApplicationRoleResponse
import `in`.koreatech.koin.domain.model.recruitment.ApplicantDetail
import `in`.koreatech.koin.domain.model.recruitment.ApplicantList
import `in`.koreatech.koin.domain.model.recruitment.ApplicantProfileSnapshot
import `in`.koreatech.koin.domain.model.recruitment.ApplicantRecruitment
import `in`.koreatech.koin.domain.model.recruitment.ApplicantSummary
import `in`.koreatech.koin.domain.model.recruitment.ApplicationRole

fun ApplicationRoleResponse.toApplicationRole() = ApplicationRole(
    id = id,
    name = name
)

fun ApplicantRecruitmentResponse.toApplicantRecruitment() = ApplicantRecruitment(
    id = id,
    category = category,
    title = title,
    meetingType = meetingType,
    activityStartDate = activityStartDate,
    activityEndDate = activityEndDate,
    deadlineDate = deadlineDate,
    dDay = dDay,
    status = status,
    recruitmentType = recruitmentType,
    currentParticipants = currentParticipants,
    maxParticipants = maxParticipants,
    roles = roles.map { it.toRecruitmentRole() },
    teamChatAvailable = teamChatAvailable,
    teamChatRoomId = teamChatRoomId
)

fun ApplicantSummaryResponse.toApplicantSummary() = ApplicantSummary(
    applicationId = applicationId,
    nickname = nickname,
    department = department,
    studentYear = studentYear,
    role = role?.toApplicationRole(),
    status = status,
    canOpenDirectChat = canOpenDirectChat
)

fun ApplicantListResponse.toApplicantList() = ApplicantList(
    recruitment = recruitment.toApplicantRecruitment(),
    applications = applications.map { it.toApplicantSummary() },
    totalCount = totalCount,
    currentCount = currentCount,
    totalPage = totalPage,
    currentPage = currentPage
)

fun ApplicantDetailResponse.toApplicantDetail() = ApplicantDetail(
    applicationId = applicationId,
    status = status,
    profileSnapshot = ApplicantProfileSnapshot(
        nickname = profileSnapshot.nickname,
        department = profileSnapshot.department,
        studentYear = profileSnapshot.studentYear,
        preferredRole = profileSnapshot.preferredRole,
        skills = profileSnapshot.skills,
        activities = profileSnapshot.activities.map { it.toTeamRecruitmentActivity() },
        selfIntroduction = profileSnapshot.selfIntroduction
    ),
    motivation = motivation,
    availability = availability,
    role = role?.toApplicationRole(),
    canDecide = canDecide,
    canOpenDirectChat = canOpenDirectChat
)
