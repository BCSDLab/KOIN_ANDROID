package `in`.koreatech.koin.feature.store.view

import `in`.koreatech.koin.domain.model.cart.Cart

data class CartState (
    val cart: Cart = Cart.Empty,
)