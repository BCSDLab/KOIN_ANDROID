package `in`.koreatech.koin.domain.usecase.chat

import `in`.koreatech.koin.domain.model.chat.ChatRoomMessage
import `in`.koreatech.koin.domain.repository.ChatRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class SubscribeLostItemChatRoomUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(articleId: Int, chatRoomId: Int): Flow<ChatRoomMessage> =
        chatRepository.subscribeLostItemChatRoom(articleId, chatRoomId)
}
