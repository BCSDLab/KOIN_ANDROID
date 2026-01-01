package `in`.koreatech.koin.feature.store.nearby

import `in`.koreatech.koin.feature.store.enums.MinimumPriceOption
import `in`.koreatech.koin.feature.store.enums.OrderOption
import `in`.koreatech.koin.feature.store.enums.StoreFilter
import `in`.koreatech.koin.feature.store.model.LocalShop
import `in`.koreatech.koin.feature.store.model.LocalStoreCategories
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class StoreNearbyState(
    val isLoading: Boolean = true,
    val categoryId: Int = -1,
    val showSearch: Boolean = false,
    val query: String = "",
    val storeCategories: ImmutableList<LocalStoreCategories> = persistentListOf(),
    val orderableShops: ImmutableList<LocalShop> = persistentListOf(),
    val showOrderOptions: Boolean = false,
    val showMinimumPriceOptions: Boolean = false,
    val selectedOrderOption: OrderOption = OrderOption.NONE,
    val selectedStoreFilter: ImmutableList<StoreFilter> = persistentListOf(StoreFilter.IS_OPEN),
    val selectedMinimumPriceOption: MinimumPriceOption = MinimumPriceOption.ALL,
    val cartItemCount: Int = 0,
    val isLoggedIn: Boolean = false,
    val showSignInDialog: Boolean = false
)
