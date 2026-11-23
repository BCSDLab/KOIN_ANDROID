package `in`.koreatech.koin.domain.usecase.chat

import `in`.koreatech.koin.domain.model.chat.ChatRoomMessage
import `in`.koreatech.koin.domain.repository.ChatRepository
import javax.inject.Inject

class GetChatRoomMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(
        roomId: String,
        afterMessageId: String? = null,
        beforeMessageId: String? = null,
        limit: Int = 100
    ): Result<List<ChatRoomMessage>> =
        chatRepository.getChatRoomMessages(roomId, afterMessageId, beforeMessageId, limit)
}
