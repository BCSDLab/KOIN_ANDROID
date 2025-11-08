package `in`.koreatech.koin.feature.store.detail

sealed class StoreDetailSideEffect {
    data object NavigateToCart : StoreDetailSideEffect()
    data object CheckCallPermission : StoreDetailSideEffect()
    data object PermissionDenied : StoreDetailSideEffect()
}
