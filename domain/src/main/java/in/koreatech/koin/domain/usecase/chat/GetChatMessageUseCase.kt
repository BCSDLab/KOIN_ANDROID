package `in`.koreatech.koin.domain.usecase.chat

import `in`.koreatech.koin.domain.model.chat.ChatMessage
import `in`.koreatech.koin.domain.repository.ChatRepository
import javax.inject.Inject

class GetChatMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(
        articleId: Int,
        chatRoomId: Int
    ): Result<List<ChatMessage>> {
        return chatRepository.getChatMessages(articleId, chatRoomId)
    }
}
