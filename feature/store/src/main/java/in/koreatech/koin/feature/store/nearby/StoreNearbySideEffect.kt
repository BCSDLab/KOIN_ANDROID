package `in`.koreatech.koin.feature.store.nearby

sealed class StoreNearbySideEffect {
    data object NavigateToCart : StoreNearbySideEffect()
    data object FetchData : StoreNearbySideEffect()
    data class ScrollCategory(val categoryId: Int) : StoreNearbySideEffect()
    data object ScrollToTop : StoreNearbySideEffect()
}
