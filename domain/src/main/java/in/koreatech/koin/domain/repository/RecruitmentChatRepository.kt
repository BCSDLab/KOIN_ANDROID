package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.recruitment.chat.RecruitmentChatMessage
import `in`.koreatech.koin.domain.model.recruitment.chat.RecruitmentChatRoom

interface RecruitmentChatRepository {
    suspend fun getChatRoom(recruitmentId: Int, chatRoomId: Int): Result<RecruitmentChatRoom>

    suspend fun createOrGetDirectChatRoom(recruitmentId: Int, applicationId: Int): Result<RecruitmentChatRoom>

    suspend fun getMessages(
        recruitmentId: Int,
        chatRoomId: Int,
        afterMessageId: Int?,
        beforeMessageId: Int?,
        limit: Int
    ): Result<List<RecruitmentChatMessage>>

    suspend fun sendMessage(
        recruitmentId: Int,
        chatRoomId: Int,
        content: String,
        isImage: Boolean
    ): Result<RecruitmentChatMessage>
}
