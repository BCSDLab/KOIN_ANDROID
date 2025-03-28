package `in`.koreatech.koin.domain.usecase.chat

import `in`.koreatech.koin.domain.repository.ChatRepository
import javax.inject.Inject
import kotlinx.coroutines.CancellationException

class ChatWSConnectUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return runCatching {
            chatRepository.connectWS()
        }.onFailure {
            if (it is CancellationException) throw it
        }
    }
}
