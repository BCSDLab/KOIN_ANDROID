package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.response.recruitment.RecruitmentResponse
import `in`.koreatech.koin.data.response.recruitment.RecruitmentRoleResponse
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentPostInfo
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentRoleInfo

fun RecruitmentResponse.toRecruitmentPostInfo() = RecruitmentPostInfo(
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
    roles = roles.map { it.toRecruitmentRoleInfo() },
    applicantCount = applicantCount,
    canClose = canClose,
    teamChatAvailable = teamChatAvailable,
    teamChatRoomId = teamChatRoomId
)

fun RecruitmentRoleResponse.toRecruitmentRoleInfo() = RecruitmentRoleInfo(
    id = id,
    name = name,
    currentParticipants = currentParticipants,
    maxParticipants = maxParticipants,
    isClosed = isClosed
)
