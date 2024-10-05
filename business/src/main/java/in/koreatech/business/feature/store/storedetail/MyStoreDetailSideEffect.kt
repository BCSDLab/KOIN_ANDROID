package `in`.koreatech.business.feature.store.storedetail


sealed class MyStoreDetailSideEffect {
    data object NavigateToUploadEventScreen : MyStoreDetailSideEffect()
    data object NavigateToModifyScreen : MyStoreDetailSideEffect()
    data object NavigateToManageMenuScreen :  MyStoreDetailSideEffect()
    data object NavigateToRegisterMenuScreen :  MyStoreDetailSideEffect()

    data class NavigateToModifyMenuScreen(val menuId: Int) : MyStoreDetailSideEffect()
    data object NavigateToRegisterStoreScreen :  MyStoreDetailSideEffect()
    data class ShowErrorMessage(val errorMessage: String) : MyStoreDetailSideEffect()
    data object ShowErrorModifyEventToast : MyStoreDetailSideEffect()
}