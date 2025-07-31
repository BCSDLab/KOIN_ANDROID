package `in`.koreatech.koin.feature.store.view.cart.edit

sealed class CartEditSideEffect {
    data object UnknownError : CartEditSideEffect()
    data object CartItemUpdated : CartEditSideEffect()
}
