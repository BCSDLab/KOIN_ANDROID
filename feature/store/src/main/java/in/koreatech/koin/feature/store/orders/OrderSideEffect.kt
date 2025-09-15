package `in`.koreatech.koin.feature.store.orders

sealed class OrderSideEffect {
    data object NavigateToCart : OrderSideEffect()
}
