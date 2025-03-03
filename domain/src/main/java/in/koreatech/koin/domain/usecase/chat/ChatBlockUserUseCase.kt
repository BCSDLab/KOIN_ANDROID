package `in`.koreatech.koin.domain.usecase.chat

import `in`.koreatech.koin.domain.repository.ChatRepository
import javax.inject.Inject

class ChatBlockUserUseCase
    @Inject
    constructor(
        private val chatRepository: ChatRepository,
    ) {
        suspend operator fun invoke(
            articleId: Int,
            chatRoomId: Int,
        ): Result<Unit> {
            return chatRepository.blockUser(articleId, chatRoomId)
        }
    }
