package `in`.koreatech.koin.domain.usecase.callvan

import `in`.koreatech.koin.domain.model.callvan.CallvanChatMessage
import `in`.koreatech.koin.domain.repository.CallvanRepository
import javax.inject.Inject

class GetCallvanChatMessagesUseCase @Inject constructor(
    private val callvanRepository: CallvanRepository
) {
    suspend operator fun invoke(
        postId: Int
    ): Result<CallvanChatMessage> = callvanRepository.getCallvanChatMessages(
        postId = postId
    )
}
