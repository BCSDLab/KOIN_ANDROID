package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.error.user.UserErrorHandler
import `in`.koreatech.koin.domain.model.store.Review
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class WriteReviewUseCase @Inject constructor(
    private val storeRepository: StoreRepository,
) {
    suspend operator fun invoke(storeId: Int, content: Review) {
        storeRepository.writeReview(storeId, content)
    }
}

