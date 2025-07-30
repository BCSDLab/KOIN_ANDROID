package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.Shop
import `in`.koreatech.koin.domain.model.store.StoreSorter
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class GetOrderableShopsUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(
        sorter: StoreSorter = StoreSorter.NONE,
        filter: List<String>? = null,
        minimumOrderAmount: Int? = null,
        categoryId: Int = 1
    ): Result<List<Shop>> {
        return storeRepository.getOrderableShops(
            sorter = sorter.name,
            filter = filter ?: emptyList(),
            categoryFilter = categoryId,
            minimumOrderAmount = if (minimumOrderAmount == Int.MAX_VALUE) null else minimumOrderAmount
        )
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

private fun List<Shop>.filterByStoreFilter(
    filter: List<String>?
): List<Shop> {
    return if (filter.isNullOrEmpty()) {
        this
    } else {
        val isOpen = filter.contains("IS_OPEN")
        val isDeliveryAvailable = filter.contains("DELIVERY_AVAILABLE")
        val isTakeoutAvailable = filter.contains("TAKEOUT_AVAILABLE")
        val isFreeDeliveryTip = filter.contains("FREE_DELIVERY_TIP")

        this.filter {
            if (isOpen) it.isOpen else true
        }.filter {
            if (isDeliveryAvailable) it.isDeliveryAvailable else true
        }.filter {
            if (isTakeoutAvailable) it.isTakeoutAvailable else true
        }.filter {
            if (isFreeDeliveryTip) it.maximumDeliveryTip == 0 else true
        }
    }
}
