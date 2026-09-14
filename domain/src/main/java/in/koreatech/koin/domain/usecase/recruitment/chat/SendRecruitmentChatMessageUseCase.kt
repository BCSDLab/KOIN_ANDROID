package `in`.koreatech.koin.domain.usecase.recruitment.chat

import `in`.koreatech.koin.domain.model.recruitment.chat.RecruitmentChatMessage
import `in`.koreatech.koin.domain.repository.RecruitmentChatRepository
import javax.inject.Inject

class SendRecruitmentChatMessageUseCase @Inject constructor(
    private val recruitmentChatRepository: RecruitmentChatRepository
) {
    suspend operator fun invoke(
        recruitmentId: Int,
        chatRoomId: Int,
        content: String,
        isImage: Boolean
    ): Result<RecruitmentChatMessage> = recruitmentChatRepository.sendMessage(
        recruitmentId = recruitmentId,
        chatRoomId = chatRoomId,
        content = content,
        isImage = isImage
    )
}
