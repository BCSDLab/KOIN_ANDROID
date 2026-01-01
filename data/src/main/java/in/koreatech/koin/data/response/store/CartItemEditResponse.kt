package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class CartItemEditResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("images") val images: List<String>,
    @SerializedName("prices") val prices: List<CartItemEditPriceResponse>,
    @SerializedName("option_groups") val optionGroups: List<CartItemEditOptionGroupResponse>
) {
    data class CartItemEditPriceResponse(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String?,
        @SerializedName("price") val price: Int,
        @SerializedName("is_selected") val isSelected: Boolean
    )

    data class CartItemEditOptionGroupResponse(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("description") val description: String,
        @SerializedName("is_required") val isRequired: Boolean,
        @SerializedName("min_select") val minSelect: Int,
        @SerializedName("max_select") val maxSelect: Int,
        @SerializedName("options") val options: List<CartItemEditOptionResponse>
    ) {
        data class CartItemEditOptionResponse(
            @SerializedName("id") val id: Int,
            @SerializedName("name") val name: String,
            @SerializedName("price") val price: Int,
            @SerializedName("is_selected") val isSelected: Boolean
        )
    }
}
