package `in`.koreatech.koin.feature.store.orders

sealed class OrderHistorySideEffect {
    data object NavigateToCart : OrderHistorySideEffect()
}
