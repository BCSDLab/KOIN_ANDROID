package `in`.koreatech.koin.feature.store.model

import android.os.Parcelable
import `in`.koreatech.koin.domain.model.store.CartItemEdit
import `in`.koreatech.koin.domain.model.store.ShopMenu.ShopMenuPrice
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class LocalShopPrice(
    val id: Int,
    val name: String?,
    val price: Int
) : Parcelable

fun ShopMenuPrice.toLocalShopPrice() = LocalShopPrice(
    id = this.id,
    name = this.name,
    price = this.price
)

fun CartItemEdit.CartItemEditPrice.toLocalShopPrice() = LocalShopPrice(
    id = this.id,
    name = this.name,
    price = this.price
)
