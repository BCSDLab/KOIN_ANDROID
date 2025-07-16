package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class ShopMenusResponse(
    @SerializedName("menu_group_id") val menuGroupId: Int,
    @SerializedName("menu_group_name") val menuGroupName: String,
    @SerializedName("menus") val menus: List<ShopMenuResponse>
) {
    data class ShopMenuResponse(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("description") val description: String,
        @SerializedName("thumbnail_image") val thumbnailImage: String,
        @SerializedName("is_sold_out") val isSoldOut: Boolean,
        @SerializedName("prices") val prices: List<ShopMenuPriceResponse>
    ) {
        data class ShopMenuPriceResponse(
            @SerializedName("id") val id: Int,
            @SerializedName("name") val name: String?,
            @SerializedName("price") val price: Int
        )
    }
}
