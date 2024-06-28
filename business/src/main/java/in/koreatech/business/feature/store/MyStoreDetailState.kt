package `in`.koreatech.business.feature.store

import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreEvent
import `in`.koreatech.koin.domain.model.store.StoreMenu
import `in`.koreatech.koin.domain.model.store.StoreWithMenu

data class MyStoreDetailState (
    val storeList: List<Store> = emptyList(),
    val storeInfo: StoreWithMenu? = null,
    val storeId: Int = -1,
    val storeEvent: List<StoreEvent> = emptyList(),
    val storeMenu: List<StoreMenu> = emptyList(),
    val isEventExpanded: List<Boolean> = List(storeMenu.size) { false },
)