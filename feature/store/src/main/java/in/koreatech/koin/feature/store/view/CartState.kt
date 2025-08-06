package `in`.koreatech.koin.feature.store.view

import `in`.koreatech.koin.domain.model.cart.Cart
import `in`.koreatech.koin.domain.model.cart.CartType
import `in`.koreatech.koin.domain.model.cart.CartValidate
import `in`.koreatech.koin.feature.store.enums.CartValidation

data class CartState(
    val cart: Cart = Cart.Empty,
    val isValidateCart: CartValidate = CartValidate.Empty,
    val cartValidation: CartValidation = CartValidation.NONE,
    val cartType: CartType = CartType.DELIVERY,
    val showDeleteDialog: Boolean = false,
    val minimumOrderAmount: Int = 0,
    val isLoading: Boolean = false
) {
    val totalItemCount: Int
        get() = cart.items.count() * cart.items.sumOf { it.quantity }
}
