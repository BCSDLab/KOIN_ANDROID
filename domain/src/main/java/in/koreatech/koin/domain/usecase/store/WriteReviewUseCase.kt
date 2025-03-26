package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.Review
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class WriteReviewUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(
        storeId: Int,
        content: Review
    ): Result<Unit> {
        return try {
            storeRepository.writeReview(storeId, content)
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        }  catch (e: Exception) {
            Result.failure(e)
        }
    }
}
