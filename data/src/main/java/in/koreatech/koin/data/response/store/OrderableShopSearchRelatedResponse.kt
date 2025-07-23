package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class OrderableShopSearchRelatedResponse(
    @SerializedName("search_keyword") val searchKeyword: String,
    @SerializedName("processed_search_keyword") val processedSearchKeyword: String,
    @SerializedName("shop_name_search_result_count") val shopNameSearchResultCount: Int,
    @SerializedName("menu_name_search_result_count") val menuNameSearchResultCount: Int,
    @SerializedName("shop_name_search_results") val shopNameSearchResults: List<OrderableShopSearchShopNameResult>,
    @SerializedName("menu_name_search_results") val menuNameSearchResults: List<OrderableShopSearchMenuNameResult>
) {
    data class OrderableShopSearchShopNameResult(
        @SerializedName("orderable_shop_id") val orderableShopId: Int,
        @SerializedName("orderable_shop_name") val orderableShopName: String
    )

    data class OrderableShopSearchMenuNameResult(
        @SerializedName("orderable_shop_id") val orderableShopId: Int,
        @SerializedName("orderable_shop_name") val orderableShopName: String,
        @SerializedName("menu_name") val menuName: String    )
}
