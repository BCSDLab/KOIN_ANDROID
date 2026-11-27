package `in`.koreatech.koin.domain.usecase.chat

import `in`.koreatech.koin.domain.model.chat.ChatMessage
import `in`.koreatech.koin.domain.repository.ChatRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class SubscribeChatRoomUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    @Deprecated("Use SubscribeLostItemChatRoomUseCase")
    operator fun invoke(
        articleId: Int,
        chatRoomId: Int
    ): Flow<ChatMessage> = chatRepository.subscribeChatRoom(articleId, chatRoomId)
}
