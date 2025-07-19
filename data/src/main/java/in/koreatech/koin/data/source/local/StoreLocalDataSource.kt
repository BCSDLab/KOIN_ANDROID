package `in`.koreatech.koin.data.source.local

import `in`.koreatech.koin.data.response.store.StoreCategoriesItemResponse
import `in`.koreatech.koin.domain.model.store.Shop
import javax.inject.Inject

class StoreLocalDataSource @Inject constructor() {
    private var storeCategories: List<StoreCategoriesItemResponse> = emptyList()
    private var shops: List<Shop> = emptyList()
    private var nearbyShops: List<Shop> = emptyList()

    fun setCachedStoreCategories(storeCategories: List<StoreCategoriesItemResponse>) {
        this.storeCategories = storeCategories
    }

    fun getCachedStoreCategories(): List<StoreCategoriesItemResponse>? {
        return this.storeCategories.ifEmpty { null }
    }

    fun setCachedShops(shops: List<Shop>) {
        this.shops = shops
    }

    fun getCachedShops(): List<Shop>? {
        return this.shops.ifEmpty { null }
    }

    fun setCachedNearbyShops(nearbyShops: List<Shop>) {
        this.nearbyShops = nearbyShops
    }

    fun getCachedNearbyShops(): List<Shop>? {
        return this.nearbyShops.ifEmpty { null }
    }
}
