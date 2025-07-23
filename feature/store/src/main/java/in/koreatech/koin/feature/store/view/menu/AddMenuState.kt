package `in`.koreatech.koin.feature.store.view.menu

import `in`.koreatech.koin.domain.model.store.CartAdd
import `in`.koreatech.koin.domain.model.store.CartItem
import `in`.koreatech.koin.domain.model.store.CartItemEdit
import `in`.koreatech.koin.domain.model.store.AddCartItemOption

data class AddMenuState(
    val cartItemEdit: CartItemEdit? = null,
    val cartItem: CartItem? = null,
    val cartAdd: CartAdd? = null,
    val priceId: Int = -1,
    val totalPrice: Int = 0,
    val cartItemOptioins: List<AddCartItemOption> = emptyList()
)
