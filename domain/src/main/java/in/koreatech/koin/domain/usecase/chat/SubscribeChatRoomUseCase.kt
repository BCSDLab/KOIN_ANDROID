package `in`.koreatech.koin.domain.usecase.chat

import `in`.koreatech.koin.domain.repository.ChatRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.catch

class SubscribeChatRoomUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(
        articleId: Int,
        chatRoomId: Int
    ) = chatRepository.subscribeChatRoom(articleId, chatRoomId)
}
