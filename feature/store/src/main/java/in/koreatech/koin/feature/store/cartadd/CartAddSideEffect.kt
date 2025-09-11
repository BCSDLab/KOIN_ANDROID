package `in`.koreatech.koin.feature.store.cartadd

sealed class CartAddSideEffect {
    data object CartItemAdded : CartAddSideEffect()
}
