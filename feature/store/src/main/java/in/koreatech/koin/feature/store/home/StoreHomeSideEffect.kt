package `in`.koreatech.koin.feature.store.home

sealed class StoreHomeSideEffect {
    data object NavigateToCart : StoreHomeSideEffect()
}
