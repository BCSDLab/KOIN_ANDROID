package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.owner.StoreDetailInfo
import `in`.koreatech.koin.domain.model.owner.insertstore.OperatingTime
import `in`.koreatech.koin.domain.model.owner.menu.StoreMenuInfo
import `in`.koreatech.koin.domain.model.store.ShopEvents
import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreMenu
import `in`.koreatech.koin.domain.model.store.StoreWithMenu

interface OwnerShopRepository {
    suspend fun getMyShopList(): List<Store>
    suspend fun getOwnerShopEvents(storeId: Int): ShopEvents
    suspend fun getOwnerShopMenus(storeId: Int): StoreMenu
    suspend fun getOwnerShopInfo(storeId: Int): StoreDetailInfo
    suspend fun getStoreMenuInfo(menuId: Int): StoreMenuInfo
    suspend fun deleteOwnerShopEvent(storeId: Int, eventId: Int)
    suspend fun modifyOwnerShopInfo(
        shopId: Int,
        storeDetailInfo: StoreDetailInfo,
    )
    fun getOwnerStoreSize(): Boolean
}