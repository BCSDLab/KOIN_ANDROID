package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.ReviewDetail
import `in`.koreatech.koin.domain.repository.StoreRepository
import `in`.koreatech.koin.domain.util.suspendRunCatching
import javax.inject.Inject

class SearchReviewUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(reviewId: Int, shopId: Int): Result<ReviewDetail> {
        return suspendRunCatching {
            storeRepository.searchReview(reviewId, shopId)
        }
    }
}
