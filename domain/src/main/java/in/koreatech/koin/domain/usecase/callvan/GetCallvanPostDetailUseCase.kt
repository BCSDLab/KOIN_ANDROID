package `in`.koreatech.koin.domain.usecase.callvan

import `in`.koreatech.koin.domain.model.callvan.CallvanPostDetail
import `in`.koreatech.koin.domain.repository.CallvanRepository
import javax.inject.Inject

class GetCallvanPostDetailUseCase @Inject constructor(
    private val callvanRepository: CallvanRepository
) {
    suspend operator fun invoke(
        postId: Int
    ): Result<CallvanPostDetail> = callvanRepository.getCallvanPostDetail(
        postId = postId
    )
}
