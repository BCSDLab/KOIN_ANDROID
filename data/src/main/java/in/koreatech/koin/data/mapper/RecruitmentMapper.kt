package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.response.recruitment.MyAppliedRecruitmentResponse
import `in`.koreatech.koin.data.response.recruitment.MyRecruitmentPostResponse
import `in`.koreatech.koin.data.response.recruitment.RecruitmentApplicationResponse
import `in`.koreatech.koin.data.response.recruitment.RecruitmentDetailResponse
import `in`.koreatech.koin.data.response.recruitment.RecruitmentListResponse
import `in`.koreatech.koin.data.response.recruitment.RecruitmentNotificationListResponse
import `in`.koreatech.koin.data.response.recruitment.RecruitmentNotificationResponse
import `in`.koreatech.koin.data.response.recruitment.RecruitmentResponse
import `in`.koreatech.koin.data.response.recruitment.RecruitmentRoleResponse
import `in`.koreatech.koin.domain.model.recruitment.MyAppliedRecruitment
import `in`.koreatech.koin.domain.model.recruitment.MyRecruitmentPost
import `in`.koreatech.koin.domain.model.recruitment.Recruitment
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentApplication
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentDetail
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentNotification
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentNotifications
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentRole
import `in`.koreatech.koin.domain.model.recruitment.Recruitments

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
