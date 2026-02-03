package `in`.koreatech.koin.domain.usecase.chat

import `in`.koreatech.koin.domain.repository.ChatRepository
import javax.inject.Inject

class SubscribeChatListUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(
        userId: Int
    ) = chatRepository.subscribeChatList(userId)
}
