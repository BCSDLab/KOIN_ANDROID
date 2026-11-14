package `in`.koreatech.koin.domain.usecase.chat

import `in`.koreatech.koin.domain.model.chat.ChatRoomInfo
import `in`.koreatech.koin.domain.repository.ChatRepository
import javax.inject.Inject

class CreateLostItemChatRoomUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(articleId: Int): Result<ChatRoomInfo> =
        chatRepository.createLostItemChatRoom(articleId)
}
