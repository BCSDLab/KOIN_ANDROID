package `in`.koreatech.koin.domain.usecase.callvan

import `in`.koreatech.koin.domain.repository.CallvanRepository
import javax.inject.Inject

class CloseCallvanPostUseCase @Inject constructor(
    private val callvanRepository: CallvanRepository
) {
    suspend operator fun invoke(
        postId: Int
    ): Result<Unit> = callvanRepository.closeCallvanPost(
        postId = postId
    )
}
