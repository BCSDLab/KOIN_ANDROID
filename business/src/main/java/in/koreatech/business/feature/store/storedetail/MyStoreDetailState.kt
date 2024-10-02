package `in`.koreatech.business.feature.store.storedetail

import `in`.koreatech.koin.domain.model.owner.StoreDetailInfo
import `in`.koreatech.koin.domain.model.store.ShopEvent
import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreMenuCategories
import kotlinx.collections.immutable.ImmutableList

data class MyStoreDetailState(
    val storeList: List<Store> = mutableListOf(),
    val storeInfo: StoreDetailInfo? = null,
    val storeId: Int = -1,
    val storeEvent: ImmutableList<ShopEvent>? = null,
    val storeMenu: ImmutableList<StoreMenuCategories>? = null,
    val dialogVisibility: Boolean = false,
    val selectDialogVisibility: Boolean = false,
    val isEventExpanded: List<Boolean> = List(storeEvent?.size ?: 0) { false },
    val isAllEventSelected: Boolean = false,
    val isSelectedEvent: MutableList<Int> = mutableListOf(),
    val isEditMode: Boolean = false,
)