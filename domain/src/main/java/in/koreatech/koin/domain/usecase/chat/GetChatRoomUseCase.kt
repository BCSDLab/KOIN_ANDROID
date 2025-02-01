package `in`.koreatech.koin.domain.usecase.chat

import `in`.koreatech.koin.domain.repository.ChatRepository
import kotlinx.coroutines.flow.retryWhen
import javax.inject.Inject

class GetChatRoomUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(articleId: Int) =
        chatRepository.getChatRoomFromArticleId(articleId).retryWhen { cause, attempt ->
            if (attempt < 3) {
                true
            } else {
                throw cause
            }
        }
}