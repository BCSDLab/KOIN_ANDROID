package `in`.koreatech.koin.feature.store.search

import `in`.koreatech.koin.feature.store.model.LocalShopSearchResult
import `in`.koreatech.koin.feature.store.model.LocalStoreCategories
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
data class StoreSearchState(
    val isOrderableShop: Boolean = true,
    val searchQuery: String = "",
    val searchResults: List<LocalShopSearchResult> = emptyList(),
    val storeCategories: ImmutableList<LocalStoreCategories> = persistentListOf()
)
