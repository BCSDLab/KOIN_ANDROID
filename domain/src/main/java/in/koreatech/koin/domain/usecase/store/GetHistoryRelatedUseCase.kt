package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.OrderHistoryRelated
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class GetHistoryRelatedUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(
        page: Int? = null,
        limit: Int? = null,
        period: String? = null,
        status: String? = null,
        type: String? = null,
        query: String? = null
    ): Result<OrderHistoryRelated> = storeRepository.getOrderHistories(page, limit, period, status, type, query)
}
