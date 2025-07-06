package `in`.koreatech.koin.feature.store.view

import `in`.koreatech.koin.domain.model.cart.Cart
import `in`.koreatech.koin.domain.model.cart.CartValidate

data class CartState (
    val cart: Cart = Cart.Empty,
    val cartValidate: CartValidate = CartValidate.Empty,
)