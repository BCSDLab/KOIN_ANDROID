package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.Review
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject
import kotlinx.coroutines.CancellationException

class ModifyReviewUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(
        reviewId: Int,
        storeId: Int,
        content: Review
    ): Result<Unit> {
        return runCatching {
            storeRepository.modifyReview(reviewId, storeId, content)
        }.onFailure {
            if (it is CancellationException) {
                throw it
            } else {
                return Result.failure(it)
            }
        }
    }
}
