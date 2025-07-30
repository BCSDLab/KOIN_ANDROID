package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class CartItemsCountResponse(
    @SerializedName("item_type_count") val itemTypeCount: Int,
    @SerializedName("total_quantity") val totalQuantity: Int
)
