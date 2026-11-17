package `in`.koreatech.koin.domain.usecase.chat

import `in`.koreatech.koin.domain.model.chat.ChatRoomInfo
import `in`.koreatech.koin.domain.repository.ChatRepository
import javax.inject.Inject

class GetChatRoomInfoUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(roomId: String): Result<ChatRoomInfo> =
        chatRepository.getChatRoomInfo(roomId)
}
