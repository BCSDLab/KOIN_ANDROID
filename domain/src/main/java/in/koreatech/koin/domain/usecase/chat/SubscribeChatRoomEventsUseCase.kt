package `in`.koreatech.koin.domain.usecase.chat

import `in`.koreatech.koin.domain.model.chat.ChatRoomEvent
import `in`.koreatech.koin.domain.repository.ChatRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class SubscribeChatRoomEventsUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(roomId: String): Flow<ChatRoomEvent> =
        chatRepository.subscribeChatRoomEvents(roomId)
}
