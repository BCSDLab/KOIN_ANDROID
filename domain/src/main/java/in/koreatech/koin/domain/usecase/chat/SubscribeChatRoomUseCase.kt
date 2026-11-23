package `in`.koreatech.koin.domain.usecase.chat

import `in`.koreatech.koin.domain.repository.ChatRepository
import javax.inject.Inject

class SubscribeChatRoomUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    @Deprecated("")
    operator fun invoke(
        articleId: Int,
        chatRoomId: Int
    ) = chatRepository.subscribeChatRoom(articleId, chatRoomId)
}
