package `in`.koreatech.koin.domain.model.store

data class OrderableShopSearchRelated(
    val searchKeyword: String,
    val processedSearchKeyword: String,
    val shopNameSearchResultCount: Int,
    val menuNameSearchResultCount: Int,
    val shopNameSearchResults: List<OrderableShopSearchShopNameResult>,
    val menuNameSearchResults: List<OrderableShopSearchMenuNameResult>
) {
    data class OrderableShopSearchShopNameResult(
        val orderableShopId: Int,
        val orderableShopName: String
    )

    data class OrderableShopSearchMenuNameResult(
        val orderableShopId: Int,
        val orderableShopName: String,
        val menuName: String
    )
}
