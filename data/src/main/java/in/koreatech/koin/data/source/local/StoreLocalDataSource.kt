package `in`.koreatech.koin.data.source.local

import `in`.koreatech.koin.data.response.store.StoreCategoriesItemResponse
import `in`.koreatech.koin.data.response.store.StoreItemResponse
import javax.inject.Inject

class StoreLocalDataSource @Inject constructor() {
    private var storeCategories: List<StoreCategoriesItemResponse> = emptyList()
    private var nearbyShops: List<StoreItemResponse> = emptyList()

    fun setCachedStoreCategories(storeCategories: List<StoreCategoriesItemResponse>) {
        this.storeCategories = storeCategories
    }

    fun getCachedStoreCategories(): List<StoreCategoriesItemResponse>? {
        return this.storeCategories.ifEmpty { null }
    }
}
