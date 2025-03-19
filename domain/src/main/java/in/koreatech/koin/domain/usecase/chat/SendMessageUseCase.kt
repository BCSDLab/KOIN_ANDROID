package `in`.koreatech.koin.domain.usecase.chat

import `in`.koreatech.koin.domain.model.chat.ChatMessage
import `in`.koreatech.koin.domain.repository.ChatRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(
        articleId: Int,
        chatRoomId: Int,
        message: ChatMessage
    ) = chatRepository.sendMessage(articleId, chatRoomId, message)
}
