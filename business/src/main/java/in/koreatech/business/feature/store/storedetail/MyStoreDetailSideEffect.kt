package `in`.koreatech.business.feature.store.storedetail

import `in`.koreatech.business.feature.store.modifyinfo.ModifyInfoSideEffect

sealed class MyStoreDetailSideEffect {
    data object NavigateToUploadEventScreen : MyStoreDetailSideEffect()
    data object NavigateToModifyScreen : MyStoreDetailSideEffect()
    data object NavigateToManageMenuScreen :  MyStoreDetailSideEffect()
    data object NavigateToRegisterMenuScreen :  MyStoreDetailSideEffect()
    data class ShowErrorMessage(val errorMessage: String) : MyStoreDetailSideEffect()
    data object ShowErrorModifyEventToast : MyStoreDetailSideEffect()
}