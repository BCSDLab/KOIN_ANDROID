package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.store.ShopEvents
import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreMenu
import `in`.koreatech.koin.domain.model.store.StoreWithMenu

interface StoreRepository {
    suspend fun getStores(): List<Store>
    suspend fun getStoreWithMenu(storeId: Int): StoreWithMenu
    suspend fun getShopMenus(storeId: Int): StoreMenu
    suspend fun getShopEvents(storeId: Int): ShopEvents
    suspend fun invalidateStores()
}