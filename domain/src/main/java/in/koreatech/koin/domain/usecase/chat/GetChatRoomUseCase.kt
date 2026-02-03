package `in`.koreatech.koin.domain.usecase.chat

import `in`.koreatech.koin.domain.model.chat.ChatRoom
import `in`.koreatech.koin.domain.repository.ChatRepository
import javax.inject.Inject

class GetChatRoomUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(
        articleId: Int,
        chatRoomId: Int?
    ): Result<ChatRoom> {
        return if (chatRoomId == null) {
            chatRepository.getChatRoomFromArticleId(articleId)
        } else {
            chatRepository.getChatRoom(articleId, chatRoomId)
        }
    }
}
