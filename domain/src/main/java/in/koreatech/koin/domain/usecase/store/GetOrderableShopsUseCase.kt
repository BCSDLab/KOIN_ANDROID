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
        minimumOrderAmount: Int? = null,
        categoryId: Int = 1
    ): Result<List<Shop>> {
        return runCatching {
            storeRepository.getOrderableShops(sorter, filter, minimumOrderAmount).getOrThrow().filter { it.categoryIds.contains(categoryId) }
        }.onFailure {
            return Result.failure(it)
        }
    }
}
