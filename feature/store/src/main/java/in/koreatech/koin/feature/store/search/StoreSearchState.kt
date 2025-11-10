package `in`.koreatech.koin.feature.store.search

import `in`.koreatech.koin.feature.store.model.LocalShopSearchResult
import kotlinx.serialization.Serializable

@Serializable
data class StoreSearchState(
    val isOrderableShop: Boolean = true,
    val searchQuery: String = "",
    val searchResults: List<LocalShopSearchResult> = emptyList()
)
