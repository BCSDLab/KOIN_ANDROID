package `in`.koreatech.koin.data.response.cart

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.cart.CartItemOption

data class CartItemOptionResponse(
    @SerializedName("option_group_name")
    val optionGroupName: String,
    @SerializedName("option_name")
    val optionName: String,
    @SerializedName("option_price")
    val optionPrice: Int
) {
    fun toCartItemOption() =
        CartItemOption(
            optionGroupName = optionGroupName,
            optionName = optionName,
            optionPrice = optionPrice
        )
}
