package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class ShopMenuResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("images") val images: List<String>,
    @SerializedName("is_sold_out") val isSoldOut: Boolean,
    @SerializedName("prices") val prices: List<ShopMenuPriceResponse>,
    @SerializedName("option_groups") val optionGroups: List<ShopMenuOptionGroupResponse>
) {
    data class ShopMenuPriceResponse(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String?,
        @SerializedName("price") val price: Int
    )

    data class ShopMenuOptionGroupResponse(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("description") val description: String,
        @SerializedName("is_required") val isRequired: Boolean,
        @SerializedName("min_select") val minSelect: Int,
        @SerializedName("max_select") val maxSelect: Int,
        @SerializedName("options") val options: List<ShopMenuOptionResponse>
    ) {
        data class ShopMenuOptionResponse(
            @SerializedName("id") val id: Int,
            @SerializedName("name") val name: String,
            @SerializedName("price") val price: Int
        )
    }
}
