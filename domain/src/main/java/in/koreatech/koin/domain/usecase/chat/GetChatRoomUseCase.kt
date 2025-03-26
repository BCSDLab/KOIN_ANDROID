package `in`.koreatech.koin.domain.usecase.chat

import `in`.koreatech.koin.domain.repository.ChatRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.retryWhen

class GetChatRoomUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(
        articleId: Int,
        chatRoomId: Int
    ) = chatRepository.getChatRoom(articleId, chatRoomId).retryWhen { cause, attempt ->
        if (attempt < 3) {
            true
        } else {
            throw cause
        }
    }
}
