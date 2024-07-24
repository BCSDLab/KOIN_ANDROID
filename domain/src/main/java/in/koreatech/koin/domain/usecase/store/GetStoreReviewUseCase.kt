package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.StoreReview
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class GetStoreReviewUseCase @Inject constructor(
    private val storeRepository: StoreRepository,
) {
    suspend operator fun invoke(
        shopId: Int
    ): StoreReview {
        return storeRepository.getStoreReviews(shopId)
    }
}