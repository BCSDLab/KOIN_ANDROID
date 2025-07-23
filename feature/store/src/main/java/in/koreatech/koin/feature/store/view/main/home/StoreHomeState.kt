package `in`.koreatech.koin.feature.store.view.main.home

import `in`.koreatech.koin.feature.store.enums.MinimumPriceOption
import `in`.koreatech.koin.feature.store.enums.OrderOption
import `in`.koreatech.koin.feature.store.enums.StoreFilter
import `in`.koreatech.koin.feature.store.model.LocalShop
import `in`.koreatech.koin.feature.store.model.LocalStoreCategories

data class StoreHomeState(
    val isLoading: Boolean = true,
    val categoryId: Int = -1,
    val storeCategories: List<LocalStoreCategories> = listOf(),
    val orderableShops: List<LocalShop> = listOf(),
    val showOrderOptions: Boolean = false,
    val showMinimumPriceOptions: Boolean = false,
    val selectedOrderOption: OrderOption = OrderOption.NONE,
    val selectedStoreFilter: List<StoreFilter> = listOf(StoreFilter.IS_OPEN),
    val selectedMinimumPriceOption: MinimumPriceOption = MinimumPriceOption.ALL
)
