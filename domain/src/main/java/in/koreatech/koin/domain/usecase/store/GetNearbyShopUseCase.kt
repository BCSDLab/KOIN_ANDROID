package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.Shop
import `in`.koreatech.koin.domain.model.store.StoreSorter
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class GetNearbyShopUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(
        sorter: StoreSorter = StoreSorter.NONE,
        isOperating: Boolean = true,
        categoryId: Int = 1
    ): Result<List<Shop>> {
        return runCatching {
            storeRepository.getNearbyShops().getOrThrow()
                .filter { it.categoryIds.contains(categoryId) }
                .filter { if (isOperating) it.isOpen else true }
                .orderByStoreSorter(sorter)
        }.onFailure {
            return Result.failure(it)
        }
    }
}

private fun List<Shop>.orderByStoreSorter(
    storeSorter: StoreSorter
): List<Shop> {
    return when (storeSorter) {
        StoreSorter.NONE -> this
        StoreSorter.COUNT -> sortedByDescending { it.reviewCount }
        StoreSorter.RATING -> sortedByDescending { it.ratingAverage }
    }
}
