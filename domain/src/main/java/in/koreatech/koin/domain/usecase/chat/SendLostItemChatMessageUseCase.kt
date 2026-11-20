package `in`.koreatech.koin.domain.usecase.chat

import `in`.koreatech.koin.domain.model.chat.ChatRoomMessage
import `in`.koreatech.koin.domain.repository.ChatRepository
import javax.inject.Inject

class SendLostItemChatMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(
        articleId: Int,
        chatRoomId: Int,
        content: String,
        isImage: Boolean
    ): Result<ChatRoomMessage> =
        chatRepository.sendLostItemChatMessage(articleId, chatRoomId, content, isImage)
}
