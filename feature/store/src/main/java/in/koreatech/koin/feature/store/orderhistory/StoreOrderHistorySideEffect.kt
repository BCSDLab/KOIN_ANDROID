package `in`.koreatech.koin.feature.store.orderhistory

sealed class StoreOrderHistorySideEffect {
    data object NavigateToCart : StoreOrderHistorySideEffect()
}
