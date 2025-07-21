package `in`.koreatech.koin.data.source.local

import `in`.koreatech.koin.data.response.store.ShopResponse
import `in`.koreatech.koin.data.response.store.StoreCategoriesItemResponse
import `in`.koreatech.koin.data.response.store.StoreItemResponse
import javax.inject.Inject

class StoreLocalDataSource @Inject constructor() {
    private var storeCategories: List<StoreCategoriesItemResponse> = emptyList()
    private var shops: List<ShopResponse> = emptyList()
    private var nearbyShops: List<StoreItemResponse> = emptyList()

    fun setCachedStoreCategories(storeCategories: List<StoreCategoriesItemResponse>) {
        this.storeCategories = storeCategories
    }

    fun getCachedStoreCategories(): List<StoreCategoriesItemResponse>? {
        return this.storeCategories.ifEmpty { null }
    }

    fun setCachedShops(shops: List<ShopResponse>) {
        this.shops = shops
    }

    fun getCachedShops(): List<ShopResponse>? {
        return this.shops.ifEmpty { null }
    }

    fun setCachedNearbyShops(nearbyShops: List<StoreItemResponse>) {
        this.nearbyShops = nearbyShops
    }

    fun getCachedNearbyShops(): List<StoreItemResponse>? {
        return this.nearbyShops.ifEmpty { null }
    }
}
