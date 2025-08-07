package `in`.koreatech.koin.feature.store.view.main.nearby

sealed class StoreNearbySideEffect {
    data object NavigateToCart : StoreNearbySideEffect()
}
