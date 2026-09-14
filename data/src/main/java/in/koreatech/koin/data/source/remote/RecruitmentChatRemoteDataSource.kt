package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.auth.RecruitmentChatAuthApi
import `in`.koreatech.koin.data.request.recruitment.chat.CreateChatMessageRequest
import `in`.koreatech.koin.data.response.recruitment.chat.RecruitmentChatMessageResponse
import `in`.koreatech.koin.data.response.recruitment.chat.RecruitmentChatRoomResponse
import javax.inject.Inject

class RecruitmentChatRemoteDataSource @Inject constructor(
    private val recruitmentChatAuthApi: RecruitmentChatAuthApi
) {
    suspend fun getChatRoom(recruitmentId: Int, chatRoomId: Int): RecruitmentChatRoomResponse =
        recruitmentChatAuthApi.getChatRoom(recruitmentId, chatRoomId)

    suspend fun createOrGetDirectChatRoom(recruitmentId: Int, applicationId: Int): RecruitmentChatRoomResponse =
        recruitmentChatAuthApi.createOrGetDirectChatRoom(recruitmentId, applicationId)

    suspend fun getMessages(
        recruitmentId: Int,
        chatRoomId: Int,
        afterMessageId: Int?,
        beforeMessageId: Int?,
        limit: Int
    ): List<RecruitmentChatMessageResponse> = recruitmentChatAuthApi.getMessages(
        recruitmentId = recruitmentId,
        chatRoomId = chatRoomId,
        afterMessageId = afterMessageId,
        beforeMessageId = beforeMessageId,
        limit = limit
    )

    suspend fun sendMessage(
        recruitmentId: Int,
        chatRoomId: Int,
        request: CreateChatMessageRequest
    ): RecruitmentChatMessageResponse = recruitmentChatAuthApi.sendMessage(
        recruitmentId = recruitmentId,
        chatRoomId = chatRoomId,
        request = request
    )
}
