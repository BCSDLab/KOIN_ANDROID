package `in`.koreatech.business.feature.store

import `in`.koreatech.koin.domain.model.store.ShopEvent
import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreMenuCategories
import `in`.koreatech.koin.domain.model.store.StoreWithMenu
import kotlinx.collections.immutable.ImmutableList

data class MyStoreDetailState(
    val storeList: List<Store> = mutableListOf(),
    val storeInfo: StoreWithMenu? = null,
    val storeId: Int = -1,
    val storeEvent: ImmutableList<ShopEvent>? = null,
    val storeMenu: ImmutableList<StoreMenuCategories>? = null,
    val isEventExpanded: List<Boolean> = List(storeEvent?.size ?: 0) { false },
)