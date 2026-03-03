package `in`.koreatech.koin.domain.usecase.callvan

import `in`.koreatech.koin.domain.repository.CallvanRepository
import javax.inject.Inject

class ReportCallvanUserUseCase @Inject constructor(
    private val callvanRepository: CallvanRepository
) {
    suspend operator fun invoke(
        postId: Int,
        reportedUserId: Int,
        reasons: List<Pair<String, String?>>
    ): Result<Unit> = callvanRepository.reportCallvanUser(
        postId = postId,
        reportedUserId = reportedUserId,
        reasons = reasons
    )
}
