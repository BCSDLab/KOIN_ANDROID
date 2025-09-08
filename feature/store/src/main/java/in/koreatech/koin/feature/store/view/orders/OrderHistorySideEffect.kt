package `in`.koreatech.koin.feature.store.view.orders

sealed class OrderHistorySideEffect {
    data object NavigateToCart : OrderHistorySideEffect()
}
