package `in`.koreatech.koin.domain.usecase.callvan

import `in`.koreatech.koin.domain.repository.CallvanRepository
import javax.inject.Inject

class JoinCallvanPostUseCase @Inject constructor(
    private val callvanRepository: CallvanRepository
) {
    suspend operator fun invoke(
        postId: Int
    ): Result<Unit> = callvanRepository.joinCallvanPost(
        postId = postId
    )
}
