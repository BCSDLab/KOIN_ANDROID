package `in`.koreatech.koin.domain.usecase.chat

import `in`.koreatech.koin.domain.repository.ChatRepository
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class SubscribeChatRoomUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(
        articleId: Int,
        chatRoomId: Int
    ) = chatRepository.subscribeChatRoom(articleId, chatRoomId).catch {
        when (it) {
            is UninitializedPropertyAccessException -> {
                chatRepository.connectWS(retry = true)
            }

            else -> {
                throw it
            }
        }
    }
}
