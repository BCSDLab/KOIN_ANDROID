package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.OrderHistoryRelated
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class GetHistoryRelatedUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(
        page: Int,
        limit: Int,
        period: String,
        status: String,
        type: String,
        query: String
    ): Result<OrderHistoryRelated> = storeRepository.getOrderHistories(page, limit, period, status, type, query)
}
