package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.request.recruitment.TeamRecruitmentActivityRequest
import `in`.koreatech.koin.data.request.recruitment.TeamRecruitmentRoleRequest
import `in`.koreatech.koin.data.response.recruitment.MyAppliedRecruitmentResponse
import `in`.koreatech.koin.data.response.recruitment.MyRecruitmentPostResponse
import `in`.koreatech.koin.data.response.recruitment.RecruitmentResponse
import `in`.koreatech.koin.data.response.recruitment.RecruitmentRoleResponse
import `in`.koreatech.koin.data.response.recruitment.TeamRecruitmentActivityResponse
import `in`.koreatech.koin.data.response.recruitment.TeamRecruitmentApplicationResponse
import `in`.koreatech.koin.data.response.recruitment.TeamRecruitmentApplicationRoleResponse
import `in`.koreatech.koin.data.response.recruitment.TeamRecruitmentNotificationListResponse
import `in`.koreatech.koin.data.response.recruitment.TeamRecruitmentNotificationResponse
import `in`.koreatech.koin.data.response.recruitment.TeamRecruitmentProfileResponse
import `in`.koreatech.koin.domain.model.recruitment.MyAppliedRecruitment
import `in`.koreatech.koin.domain.model.recruitment.MyRecruitmentPost
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentNotification
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentNotifications
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentPost
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentRole
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentActivity
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentActivityInput
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentApplication
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentApplicationRole
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentProfile
import `in`.koreatech.koin.domain.model.recruitment.TeamRecruitmentRoleInput

fun RecruitmentResponse.toRecruitmentPost() = RecruitmentPost(
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
    recruitment = recruitment.toRecruitmentPost()
)

fun RecruitmentRoleResponse.toRecruitmentRole() = RecruitmentRole(
    id = id,
    name = name,
    currentParticipants = currentParticipants,
    maxParticipants = maxParticipants,
    isClosed = isClosed
)

fun TeamRecruitmentNotificationResponse.toRecruitmentNotification() = RecruitmentNotification(
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

fun TeamRecruitmentNotificationListResponse.toRecruitmentNotifications() = RecruitmentNotifications(
    notifications = notifications.map { it.toRecruitmentNotification() },
    unreadCount = unreadCount,
    totalCount = totalCount,
    currentCount = currentCount,
    totalPage = totalPage,
    currentPage = currentPage
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
