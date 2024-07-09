package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.store.ShopEvents
import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreMenu
import `in`.koreatech.koin.domain.model.store.StoreWithMenu

interface  OwnerShopRepository {
    suspend fun getMyShopList(): List<Store>
    suspend fun getOwnerShopEvents(storeId: Int): ShopEvents
    suspend fun getOwnerShopMenus(storeId: Int): StoreMenu
    suspend fun getOwnerShopInfo(storeId: Int): StoreWithMenu
}