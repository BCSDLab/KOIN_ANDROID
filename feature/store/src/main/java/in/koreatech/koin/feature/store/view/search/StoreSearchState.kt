package `in`.koreatech.koin.feature.store.view.search

import `in`.koreatech.koin.feature.store.model.LocalShopSearchResult

data class StoreSearchState(
    val searchQuery: String = "",
    val searchResults: List<LocalShopSearchResult> = emptyList()
)
