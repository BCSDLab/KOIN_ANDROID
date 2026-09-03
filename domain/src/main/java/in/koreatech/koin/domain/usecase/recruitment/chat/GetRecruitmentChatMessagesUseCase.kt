package `in`.koreatech.koin.domain.usecase.recruitment.chat

import `in`.koreatech.koin.domain.model.recruitment.chat.RecruitmentChatMessage
import `in`.koreatech.koin.domain.repository.RecruitmentChatRepository
import javax.inject.Inject

class GetRecruitmentChatMessagesUseCase @Inject constructor(
    private val recruitmentChatRepository: RecruitmentChatRepository
) {
    suspend operator fun invoke(
        recruitmentId: Int,
        chatRoomId: Int,
        afterMessageId: Int?,
        beforeMessageId: Int?,
        limit: Int = DEFAULT_LIMIT
    ): Result<List<RecruitmentChatMessage>> = recruitmentChatRepository.getMessages(
        recruitmentId = recruitmentId,
        chatRoomId = chatRoomId,
        afterMessageId = afterMessageId,
        beforeMessageId = beforeMessageId,
        limit = limit
    )

    companion object {
        const val DEFAULT_LIMIT = 100
    }
}
