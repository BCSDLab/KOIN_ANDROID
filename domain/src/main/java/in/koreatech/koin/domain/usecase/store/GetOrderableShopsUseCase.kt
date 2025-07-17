package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.Shop
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class GetOrderableShopsUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(
        sorter: String? = null,
        filter: List<String>? = null,
        minimumOrderAmount: Int? = null
    ): Result<List<Shop>> = storeRepository.getOrderableShops(sorter, filter, minimumOrderAmount)
}
