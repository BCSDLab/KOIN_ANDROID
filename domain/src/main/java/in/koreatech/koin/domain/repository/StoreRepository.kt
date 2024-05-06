package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreCategories
import `in`.koreatech.koin.domain.model.store.StoreEvent
import `in`.koreatech.koin.domain.model.store.StoreMenu
import `in`.koreatech.koin.domain.model.store.StoreWithMenu

interface StoreRepository {
    suspend fun getStores(): List<Store>
    suspend fun getStoreEvents(): List<StoreEvent>
    suspend fun getStoreCategories(): List<StoreCategories>
    suspend fun getStoreWithMenu(storeId: Int): StoreWithMenu
    suspend fun getShopMenus(storeId: Int): StoreMenu
    suspend fun invalidateStores()
}