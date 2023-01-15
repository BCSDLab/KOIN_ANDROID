package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreMenu

interface StoreRepository {
    suspend fun getStores(): List<Store>
    suspend fun getStoreMenu(store: Store): List<StoreMenu>
    suspend fun invalidateStores()
}