package `in`.koreatech.business.feature.store

import `in`.koreatech.koin.domain.model.store.ShopEvents
import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreMenuCategories
import `in`.koreatech.koin.domain.model.store.StoreWithMenu

data class MyStoreDetailState(
    val storeList: List<Store> = mutableListOf(),
    val storeInfo: StoreWithMenu? = null,
    val storeId: Int = -1,
    val storeEvent: ShopEvents? = null,
    val storeMenu: List<StoreMenuCategories>? = mutableListOf(),
    val isEventExpanded: List<Boolean> = List(storeEvent?.events?.size ?: 0) { false },
)