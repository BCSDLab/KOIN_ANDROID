package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.Review
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class ModifyReviewUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(reviewId:Int, storeId: Int, content: Review) {
        storeRepository.modifyReview(reviewId, storeId, content)
    }
}

