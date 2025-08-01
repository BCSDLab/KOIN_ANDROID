package `in`.koreatech.koin.feature.store.view.cart.add

import android.os.Parcelable
import `in`.koreatech.koin.feature.store.enums.CartError
import `in`.koreatech.koin.feature.store.model.LocalShopMenuOptionGroup
import `in`.koreatech.koin.feature.store.model.LocalShopPrice
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartAddState(
    val isLoading: Boolean = false,
    val orderableShopId: Int = -1,
    val orderableShopMenuId: Int = -1,
    val orderableShopMenuPriceId: Int = -1,
    val menuName: String = "",
    val menuDescription: String = "",
    val menuImageUrls: List<String> = emptyList(),
    val prices: List<LocalShopPrice> = emptyList(),
    val options: List<LocalShopMenuOptionGroup> = emptyList(),
    val cartItemCount: Int = 0,
    val error: CartError = CartError.NONE,
    val showErrorDialog: Boolean = false,
    val quantity: Int = 1
) : Parcelable {
    val price: Int
        get() = (
            (prices.firstOrNull { it.id == orderableShopMenuPriceId }?.price ?: 0) + options.sumOf { optionGroup ->
                optionGroup.options.filter { it.optionSelected }.sumOf { it.price }
            }
            ) * quantity

    val isButtonEnabled: Boolean
        get() = options.all { optionGroup ->
            optionGroup.minSelect <= optionGroup.options.count { it.optionSelected } &&
                optionGroup.maxSelect >= optionGroup.options.count { it.optionSelected }
        } && orderableShopMenuPriceId != -1
}
