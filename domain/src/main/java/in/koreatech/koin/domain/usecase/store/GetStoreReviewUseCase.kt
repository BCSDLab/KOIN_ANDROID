package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.StoreReview
import `in`.koreatech.koin.domain.repository.StoreRepository
import `in`.koreatech.koin.domain.util.suspendRunCatching
import javax.inject.Inject

class GetStoreReviewUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(shopId: Int): Result<StoreReview> {
        return suspendRunCatching {
            storeRepository.getStoreReviews(shopId)
        }
    }
}
