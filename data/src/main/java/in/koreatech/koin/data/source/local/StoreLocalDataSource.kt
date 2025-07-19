package `in`.koreatech.koin.data.source.local

import `in`.koreatech.koin.data.response.store.StoreCategoriesItemResponse
import javax.inject.Inject

class StoreLocalDataSource @Inject constructor() {
    private var storeCategories: List<StoreCategoriesItemResponse> = emptyList()

    fun setCachedStoreCategories(storeCategories: List<StoreCategoriesItemResponse>) {
        this.storeCategories = storeCategories
    }

    fun getCachedStoreCategories(): List<StoreCategoriesItemResponse>? {
        return this.storeCategories.ifEmpty { null }
    }
}
