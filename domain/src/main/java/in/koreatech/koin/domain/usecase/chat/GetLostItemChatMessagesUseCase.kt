package `in`.koreatech.koin.domain.usecase.chat

import `in`.koreatech.koin.domain.model.chat.ChatRoomMessage
import `in`.koreatech.koin.domain.repository.ChatRepository
import javax.inject.Inject

class GetLostItemChatMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(articleId: Int, chatRoomId: Int): Result<List<ChatRoomMessage>> =
        chatRepository.getLostItemChatMessages(articleId, chatRoomId)
}
