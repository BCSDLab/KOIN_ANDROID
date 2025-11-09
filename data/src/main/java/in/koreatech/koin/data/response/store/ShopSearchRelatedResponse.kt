package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class ShopSearchRelatedResponse(
    @SerializedName("search_keyword") val searchKeyword: String,
    @SerializedName("processed_search_keyword") val processedSearchKeyword: String,
    @SerializedName("shop_name_search_result_count") val shopNameSearchResultCount: Int,
    @SerializedName("menu_name_search_result_count") val menuNameSearchResultCount: Int,
    @SerializedName("shop_name_search_results") val shopNameSearchResults: List<ShopSearchShopNameResult>,
    @SerializedName("menu_name_search_results") val menuNameSearchResults: List<ShopSearchMenuNameResult>
) {
    data class ShopSearchShopNameResult(
        @SerializedName("shop_id") val shopId: Int,
        @SerializedName("shop_name") val shopName: String
    )

    data class ShopSearchMenuNameResult(
        @SerializedName("shop_id") val shopId: Int,
        @SerializedName("shop_name") val shopName: String,
        @SerializedName("menu_name") val menuName: String
    )
}
