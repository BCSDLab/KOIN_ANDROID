package `in`.koreatech.koin.feature.store.view.cart.add

import android.os.Parcelable
import `in`.koreatech.koin.feature.store.model.LocalShopMenuOptionGroup
import `in`.koreatech.koin.feature.store.model.LocalShopPrice
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartAddState(
    val orderableShopId: Int = -1,
    val orderableShopMenuId: Int = -1,
    val orderableShopMenuPriceId: Int = -1,
    val menuName: String = "",
    val menuDescription: String = "",
    val menuImageUrls: List<String> = emptyList(),
    val prices: List<LocalShopPrice> = emptyList(),
    val options: List<LocalShopMenuOptionGroup> = emptyList(),
    val cartItemCount: Int = 0,
    val quantity: Int = 1
) : Parcelable {
    val price: Int
        get() = (prices.firstOrNull { it.id == orderableShopMenuPriceId }?.price ?: 0) + options.sumOf { optionGroup ->
            optionGroup.options.filter { it.optionSelected }.sumOf { it.price }
        }
}
