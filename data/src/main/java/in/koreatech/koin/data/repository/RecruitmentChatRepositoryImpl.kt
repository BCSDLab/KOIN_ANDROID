package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toRecruitmentChatMessage
import `in`.koreatech.koin.data.mapper.toRecruitmentChatRoom
import `in`.koreatech.koin.data.request.recruitment.chat.CreateChatMessageRequest
import `in`.koreatech.koin.data.source.remote.RecruitmentChatRemoteDataSource
import `in`.koreatech.koin.data.util.mapHttpFailure
import `in`.koreatech.koin.domain.error.recruitment.KoinRecruitmentChatException
import `in`.koreatech.koin.domain.model.recruitment.chat.RecruitmentChatMessage
import `in`.koreatech.koin.domain.model.recruitment.chat.RecruitmentChatRoom
import `in`.koreatech.koin.domain.repository.RecruitmentChatRepository
import `in`.koreatech.koin.domain.util.suspendRunCatching
import javax.inject.Inject

class RecruitmentChatRepositoryImpl @Inject constructor(
    private val recruitmentChatRemoteDataSource: RecruitmentChatRemoteDataSource
) : RecruitmentChatRepository {
    override suspend fun getChatRoom(recruitmentId: Int, chatRoomId: Int): Result<RecruitmentChatRoom> {
        return suspendRunCatching {
            recruitmentChatRemoteDataSource.getChatRoom(recruitmentId, chatRoomId).toRecruitmentChatRoom()
        }.mapHttpFailure {
            on(401) throws KoinRecruitmentChatException.UnauthorizedException()
            on(403) throws KoinRecruitmentChatException.ChatMemberForbiddenException()
            on(404) throws KoinRecruitmentChatException.ChatRoomNotFoundException()
        }
    }

    override suspend fun createOrGetDirectChatRoom(recruitmentId: Int, applicationId: Int): Result<RecruitmentChatRoom> {
        return suspendRunCatching {
            recruitmentChatRemoteDataSource.createOrGetDirectChatRoom(recruitmentId, applicationId).toRecruitmentChatRoom()
        }.mapHttpFailure {
            on(401) throws KoinRecruitmentChatException.UnauthorizedException()
            on(403) throws KoinRecruitmentChatException.DirectChatForbiddenException()
            on(404) throws KoinRecruitmentChatException.ApplicationNotFoundException()
            on(409) throws KoinRecruitmentChatException.DirectChatConflictException()
        }
    }

    override suspend fun getMessages(
        recruitmentId: Int,
        chatRoomId: Int,
        afterMessageId: Int?,
        beforeMessageId: Int?,
        limit: Int
    ): Result<List<RecruitmentChatMessage>> {
        return suspendRunCatching {
            recruitmentChatRemoteDataSource.getMessages(
                recruitmentId = recruitmentId,
                chatRoomId = chatRoomId,
                afterMessageId = afterMessageId,
                beforeMessageId = beforeMessageId,
                limit = limit
            ).map { it.toRecruitmentChatMessage() }
        }.mapHttpFailure {
            on(400) throws KoinRecruitmentChatException.InvalidParameterException()
            on(401) throws KoinRecruitmentChatException.UnauthorizedException()
            on(403) throws KoinRecruitmentChatException.ChatMemberForbiddenException()
            on(404) throws KoinRecruitmentChatException.ChatRoomNotFoundException()
        }
    }

    override suspend fun sendMessage(
        recruitmentId: Int,
        chatRoomId: Int,
        content: String,
        isImage: Boolean
    ): Result<RecruitmentChatMessage> {
        return suspendRunCatching {
            recruitmentChatRemoteDataSource.sendMessage(
                recruitmentId = recruitmentId,
                chatRoomId = chatRoomId,
                request = CreateChatMessageRequest(content = content, isImage = isImage)
            ).toRecruitmentChatMessage()
        }.mapHttpFailure {
            on(400, "NOT_READABLE_HTTP_MESSAGE") throws KoinRecruitmentChatException.NotReadableHttpMessageException()
            on(400, "INVALID_REQUEST_BODY") throws KoinRecruitmentChatException.InvalidRequestBodyException()
            on(401) throws KoinRecruitmentChatException.UnauthorizedException()
            on(403) throws KoinRecruitmentChatException.ChatMemberForbiddenException()
            on(404) throws KoinRecruitmentChatException.ChatRoomNotFoundException()
            on(409, "TEAM_RECRUITMENT_CHAT_READ_ONLY") throws KoinRecruitmentChatException.ChatReadOnlyException()
            on(409, "REQUEST_TOO_FAST") throws KoinRecruitmentChatException.RequestTooFastException()
        }
    }
}
