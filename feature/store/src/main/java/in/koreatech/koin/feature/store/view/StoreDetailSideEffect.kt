package `in`.koreatech.koin.feature.store.view

sealed class StoreDetailSideEffect {
    data object NavigateToBack: StoreDetailSideEffect()
    data object NavigateToShoppingCart : StoreDetailSideEffect()
    data object NavigateToReview: StoreDetailSideEffect()
    data object NavigateToDetailInfo : StoreDetailSideEffect()
}
