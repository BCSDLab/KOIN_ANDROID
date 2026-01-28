package `in`.koreatech.koin.data.source.local

import `in`.koreatech.koin.data.dao.StoreCategoriesDao
import `in`.koreatech.koin.data.db.AppDatabase
import `in`.koreatech.koin.data.mapper.toStoreCategoriesEntity
import `in`.koreatech.koin.data.mapper.toStoreCategoriesItemResponse
import `in`.koreatech.koin.data.response.store.StoreCategoriesItemResponse
import `in`.koreatech.koin.data.response.store.StoreItemResponse
import javax.inject.Inject

class StoreLocalDataSource @Inject constructor(
    private val storeCategoriesDao: StoreCategoriesDao
) {
    private var nearbyShops: List<StoreItemResponse> = emptyList()

    suspend fun setCachedStoreCategories(storeCategories: List<StoreCategoriesItemResponse>) {
        storeCategoriesDao.insert(storeCategories.map { it.toStoreCategoriesEntity() })
    }

    suspend fun getCachedStoreCategories(): List<StoreCategoriesItemResponse> {
        return storeCategoriesDao.getAll().map { it.toStoreCategoriesItemResponse() }
    }

    fun setCachedNearbyShops(nearbyShops: List<StoreItemResponse>) {
        this.nearbyShops = nearbyShops
    }

    fun getCachedNearbyShops(): List<StoreItemResponse>? {
        return this.nearbyShops.ifEmpty { null }
    }
}
