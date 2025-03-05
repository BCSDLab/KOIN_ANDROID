package `in`.koreatech.koin.domain.usecase.chat

import `in`.koreatech.koin.domain.repository.ChatRepository
import javax.inject.Inject

class ChatWSConnectUseCase
    @Inject
    constructor(
        private val chatRepository: ChatRepository,
    ) {
        suspend operator fun invoke(): Result<Unit> {
            return try {
                chatRepository.connectWS()
                Result.success(Unit)
            } catch (t: Throwable) {
                Result.failure(t)
            }
        }
    }
