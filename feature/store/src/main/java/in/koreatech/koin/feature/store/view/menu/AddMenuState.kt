package `in`.koreatech.koin.feature.store.view.menu

import `in`.koreatech.koin.domain.model.store.AddCartItemOption
import `in`.koreatech.koin.domain.model.store.CartAdd
import `in`.koreatech.koin.domain.model.store.CartItem
import `in`.koreatech.koin.domain.model.store.CartItemEdit

data class AddMenuState(
    val orderableShopId: Int = -1,
    val orderableShopMenuId: Int = -1,
    val orderableShopMenuPriceId: Int = -1,
    val orderableShopMenuOptionIds: List<AddCartItemOption> = emptyList(),
    val cartItemEdit: CartItemEdit? = null,
    val cartItem: CartItem? = null,
    val cartAdd: CartAdd? = null,
    val totalPrice: Int = 0,
    val cartItemOptioins: List<AddCartItemOption> = emptyList(),
    val quantity: Int = 1
)
