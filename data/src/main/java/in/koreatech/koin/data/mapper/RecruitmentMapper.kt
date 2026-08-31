package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.response.recruitment.TeamRecruitmentNotificationListResponse
import `in`.koreatech.koin.data.response.recruitment.TeamRecruitmentNotificationResponse
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentNotification
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentNotifications

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
