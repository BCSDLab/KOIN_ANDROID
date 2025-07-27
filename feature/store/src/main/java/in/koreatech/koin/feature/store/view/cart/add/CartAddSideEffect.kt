package `in`.koreatech.koin.feature.store.view.cart.add

sealed class CartAddSideEffect {
    data object CartItemAdded : CartAddSideEffect()
}