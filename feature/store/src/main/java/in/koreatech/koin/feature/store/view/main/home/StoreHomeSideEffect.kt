package `in`.koreatech.koin.feature.store.view.main.home

sealed class StoreHomeSideEffect {
    data object NavigateToCart : StoreHomeSideEffect()
}
