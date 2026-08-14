package `in`.koreatech.koin.domain.usecase.chat

import `in`.koreatech.koin.domain.model.chat.ChatConnectionState
import `in`.koreatech.koin.domain.repository.ChatRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveChatConnectionStateUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(): Flow<ChatConnectionState> {
        return chatRepository.observeConnectionState()
    }
}
