package `in`.koreatech.business.feature.store.storedetail

import androidx.annotation.StringRes

sealed class MyStoreDetailSideEffect {
    data object NavigateToUploadEventScreen : MyStoreDetailSideEffect()

    data class NavigateToModifyScreen(val storeId: Int) : MyStoreDetailSideEffect()

    data class NavigateToManageMenuScreen(val storeId: Int) : MyStoreDetailSideEffect()

    data object NavigateToRegisterMenuScreen : MyStoreDetailSideEffect()

    data class NavigateToAddEventScreen(val storeId: Int) : MyStoreDetailSideEffect()

    data class NavigateToModifyMenuScreen(val menuId: Int) : MyStoreDetailSideEffect()

    data object NavigateToRegisterStoreScreen : MyStoreDetailSideEffect()

    data class ShowErrorMessage(val errorMessage: String) : MyStoreDetailSideEffect()

    data class ShowErrorMessageRes(
        @StringRes val errorMessageRes: Int
    ) : MyStoreDetailSideEffect()

    data object ShowErrorModifyEventToast : MyStoreDetailSideEffect()

    data object DeleteUser : MyStoreDetailSideEffect()
}
