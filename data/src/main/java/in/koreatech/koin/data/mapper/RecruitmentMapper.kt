package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.response.recruitment.MyAppliedRecruitmentResponse
import `in`.koreatech.koin.data.response.recruitment.MyRecruitmentPostResponse
import `in`.koreatech.koin.data.response.recruitment.RecruitmentResponse
import `in`.koreatech.koin.data.response.recruitment.RecruitmentRoleResponse
import `in`.koreatech.koin.domain.model.recruitment.MyAppliedRecruitment
import `in`.koreatech.koin.domain.model.recruitment.MyRecruitmentPost
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentPost
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentRole

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
