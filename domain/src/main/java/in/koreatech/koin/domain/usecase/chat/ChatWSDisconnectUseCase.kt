package `in`.koreatech.koin.domain.usecase.chat

import `in`.koreatech.koin.domain.repository.ChatRepository
import javax.inject.Inject

class ChatWSDisconnectUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return try {
            chatRepository.disconnectWS()
            Result.success(Unit)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }
}
