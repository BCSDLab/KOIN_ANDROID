package `in`.koreatech.koin.domain.usecase.chat

import `in`.koreatech.koin.domain.model.chat.ChatListItem
import `in`.koreatech.koin.domain.repository.ChatRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class SubscribeChatListUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    @Deprecated("Use SubscribeLostItemChatListUseCase")
    operator fun invoke(
        userId: Int
    ): Flow<List<ChatListItem>> = chatRepository.subscribeChatList(userId)
}
