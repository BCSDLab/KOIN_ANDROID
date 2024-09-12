package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.Review
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class DeleteReviewUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(reviewId:Int, storeId: Int) {
        storeRepository.deleteReview(reviewId, storeId)
    }
}

