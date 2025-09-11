package `in`.koreatech.koin.feature.store.search

import `in`.koreatech.koin.feature.store.model.LocalShopSearchResult

data class StoreSearchState(
    val searchQuery: String = "",
    val searchResults: List<LocalShopSearchResult> = emptyList()
)
