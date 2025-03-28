package `in`.koreatech.koin.domain.usecase.chat

import `in`.koreatech.koin.domain.model.chat.ChatListItem
import `in`.koreatech.koin.domain.repository.ChatRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetChatListUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(): Flow<List<ChatListItem>> {
        return chatRepository.getChatRoomList()
    }
}
