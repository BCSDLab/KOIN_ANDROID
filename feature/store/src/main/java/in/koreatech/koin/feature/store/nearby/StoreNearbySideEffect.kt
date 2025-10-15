package `in`.koreatech.koin.feature.store.nearby

sealed class StoreNearbySideEffect {
    data object NavigateToCart : StoreNearbySideEffect()
}
