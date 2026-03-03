package `in`.koreatech.koin.domain.usecase.callvan

import `in`.koreatech.koin.domain.repository.CallvanRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val callvanRepository: CallvanRepository
) {
    suspend operator fun invoke(
        postId: Int,
        isImage: Boolean,
        content: String
    ): Result<Unit> = callvanRepository.sendMessage(
        postId = postId,
        isImage = isImage,
        content = content
    )
}
