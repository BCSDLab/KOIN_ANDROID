package `in`.koreatech.business.feature.store

sealed class MyStoreDetailSideEffect {
    data object ShowDialog : MyStoreDetailSideEffect()
    data object NavigateToUploadEventScreen : MyStoreDetailSideEffect()
}