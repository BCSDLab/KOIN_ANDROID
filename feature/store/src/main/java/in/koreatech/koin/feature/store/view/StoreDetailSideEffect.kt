package `in`.koreatech.koin.feature.store.view

sealed class StoreDetailSideEffect {
    data object NavigateToCart : StoreDetailSideEffect()
}
