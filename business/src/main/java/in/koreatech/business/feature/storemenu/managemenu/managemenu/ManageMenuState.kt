package `in`.koreatech.business.feature.storemenu.managemenu.managemenu

import `in`.koreatech.koin.domain.model.store.StoreMenuCategories
import kotlinx.collections.immutable.ImmutableList

data class ManageMenuState(
    val storeId: Int = -1,
    val storeMenuList: ImmutableList<StoreMenuCategories>? = null,
)