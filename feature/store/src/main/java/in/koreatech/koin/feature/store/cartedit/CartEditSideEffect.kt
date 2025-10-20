package `in`.koreatech.koin.feature.store.cartedit

sealed class CartEditSideEffect {
    data object UnknownError : CartEditSideEffect()
    data object CartItemUpdated : CartEditSideEffect()
}
