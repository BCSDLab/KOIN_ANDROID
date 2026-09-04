package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.request.recruitment.RecruitmentUpdateRequest
import `in`.koreatech.koin.data.request.recruitment.TeamRecruitmentActivityRequest
import `in`.koreatech.koin.data.request.recruitment.TeamRecruitmentRoleRequest
import `in`.koreatech.koin.data.response.recruitment.ApplicantDetailResponse
import `in`.koreatech.koin.data.response.recruitment.ApplicantListResponse
import `in`.koreatech.koin.data.response.recruitment.ApplicantRecruitmentResponse
import `in`.koreatech.koin.data.response.recruitment.ApplicantSummaryResponse
import `in`.koreatech.koin.data.response.recruitment.ApplicationRoleResponse
import `in`.koreatech.koin.data.response.recruitment.MyAppliedRecruitmentResponse
import `in`.koreatech.koin.data.response.recruitment.MyRecruitmentPostResponse
import `in`.koreatech.koin.data.response.recruitment.RecruitmentApplicationResponse
import `in`.koreatech.koin.data.response.recruitment.RecruitmentDetailResponse
import `in`.koreatech.koin.data.response.recruitment.RecruitmentListResponse
import `in`.koreatech.koin.data.response.recruitment.RecruitmentNotificationListResponse
import `in`.koreatech.koin.data.response.recruitment.RecruitmentNotificationResponse
import `in`.koreatech.koin.data.response.recruitment.RecruitmentResponse
import `in`.koreatech.koin.data.response.recruitment.RecruitmentRoleResponse
import `in`.koreatech.koin.data.response.recruitment.TeamRecruitmentActivityResponse
import `in`.koreatech.koin.data.response.recruitment.TeamRecruitmentApplicationResponse
import `in`.koreatech.koin.data.response.recruitment.TeamRecruitmentApplicationRoleResponse
import `in`.koreatech.koin.data.response.recruitment.TeamRecruitmentProfileResponse
import `in`.koreatech.koin.domain.model.recruitment.ApplicantDetail
import `in`.koreatech.koin.domain.model.recruitment.ApplicantList
import `in`.koreatech.koin.domain.model.recruitment.ApplicantProfileSnapshot
import `in`.koreatech.koin.domain.model.recruitment.ApplicantRecruitment
import `in`.koreatech.koin.domain.model.recruitment.ApplicantSummary
import `in`.koreatech.koin.domain.model.recruitment.ApplicationRole
import `in`.koreatech.koin.domain.model.recruitment.MyAppliedRecruitment
import `in`.koreatech.koin.domain.model.recruitment.MyRecruitmentPost
import `in`.koreatech.koin.domain.model.recruitment.Recruitment
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentApplication
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentDetail
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentNotification
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentNotifications
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentRole
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentUpdate
import `in`.koreatech.koin.domain.model.recruitment.Recruitments
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentActivity
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentActivityInput
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentApplication
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentApplicationRole
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentProfile
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentRoleInput

fun MyRecruitmentPostResponse.toMyRecruitmentPost() = MyRecruitmentPost(
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
    applicantCount = applicantCount,
    canClose = canClose,
    teamChatAvailable = teamChatAvailable,
    teamChatRoomId = teamChatRoomId
)

fun MyAppliedRecruitmentResponse.toMyAppliedRecruitment() = MyAppliedRecruitment(
    applicationId = applicationId,
    status = status,
    teamChatAvailable = teamChatAvailable,
    teamChatRoomId = teamChatRoomId,
    directChatRoomId = directChatRoomId,
    roleName = roleName,
    recruitment = recruitment.toRecruitment()
)

fun RecruitmentRoleResponse.toRecruitmentRole() = RecruitmentRole(
    id = id,
    name = name,
    currentParticipants = currentParticipants,
    maxParticipants = maxParticipants,
    isClosed = isClosed
)

fun RecruitmentNotificationResponse.toRecruitmentNotification() = RecruitmentNotification(
    id = id,
    type = type,
    targetType = targetType,
    messagePreview = messagePreview,
    senderNickname = senderNickname,
    isRead = isRead,
    createdAt = createdAt,
    recruitmentId = recruitmentId,
    applicationId = applicationId,
    chatRoomId = chatRoomId
)

fun RecruitmentNotificationListResponse.toRecruitmentNotifications() = RecruitmentNotifications(
    notifications = notifications.map { it.toRecruitmentNotification() },
    unreadCount = unreadCount,
    totalCount = totalCount,
    currentCount = currentCount,
    totalPage = totalPage,
    currentPage = currentPage
)

fun RecruitmentResponse.toRecruitment() = Recruitment(
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
    roles = roles.map { it.toRecruitmentRole() }
)

fun RecruitmentListResponse.toRecruitments() = Recruitments(
    recruitments = recruitments.map { it.toRecruitment() },
    totalCount = totalCount,
    currentCount = currentCount,
    totalPage = totalPage,
    currentPage = currentPage
)

fun RecruitmentApplicationResponse.toRecruitmentApplication() = RecruitmentApplication(
    applicationId = applicationId,
    status = status
)

fun RecruitmentDetailResponse.toRecruitmentDetail() = RecruitmentDetail(
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
    authorNickname = authorNickname,
    description = description,
    relatedUrl = relatedUrl,
    qualification = qualification,
    createdAt = createdAt,
    isAuthor = isAuthor,
    canApply = canApply,
    applyBlockReason = applyBlockReason,
    application = application?.toRecruitmentApplication(),
    canManageApplicants = canManageApplicants,
    teamChatAvailable = teamChatAvailable,
    teamChatRoomId = teamChatRoomId
)

fun RecruitmentUpdate.toRecruitmentUpdateRequest() = RecruitmentUpdateRequest(
    category = category,
    title = title,
    meetingType = meetingType,
    activityStartDate = activityStartDate,
    activityEndDate = activityEndDate,
    deadlineDate = deadlineDate,
    recruitmentType = recruitmentType,
    maxParticipants = maxParticipants,
    roles = roles.map { role ->
        RecruitmentUpdateRequest.RecruitmentUpdateRoleRequest(
            id = role.id,
            name = role.name,
            maxParticipants = role.maxParticipants
        )
    },
    description = description,
    relatedUrl = relatedUrl,
    qualification = qualification
)

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
