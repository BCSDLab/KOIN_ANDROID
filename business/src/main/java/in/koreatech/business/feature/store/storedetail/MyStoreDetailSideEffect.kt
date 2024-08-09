package `in`.koreatech.business.feature.store.storedetail

sealed class MyStoreDetailSideEffect {
    data object NavigateToUploadEventScreen : MyStoreDetailSideEffect()
    data object NavigateToModifyScreen : MyStoreDetailSideEffect()
    data class ShowErrorMessage(val errorMessage: String) : MyStoreDetailSideEffect()
    data object ShowErrorModifyEventToast : MyStoreDetailSideEffect()
}