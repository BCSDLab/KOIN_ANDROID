package `in`.koreatech.koin.domain.usecase.chat

import `in`.koreatech.koin.domain.model.chat.ChatRoomInfo
import `in`.koreatech.koin.domain.repository.ChatRepository
import javax.inject.Inject

class GetLostItemChatRoomUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(articleId: Int, chatRoomId: Int?): Result<ChatRoomInfo> =
        chatRepository.getLostItemChatRoom(articleId, chatRoomId)
}
