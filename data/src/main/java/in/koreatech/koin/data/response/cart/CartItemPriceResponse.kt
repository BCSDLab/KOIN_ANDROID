package `in`.koreatech.koin.data.response.cart

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.cart.CartItemPrice

data class CartItemPriceResponse(
    @SerializedName("name")
    val name: String?,
    @SerializedName("price")
    val price: Int
) {
    fun toCartItemPrice() =
        CartItemPrice(
            name = name ?: "",
            price = price
        )
}
