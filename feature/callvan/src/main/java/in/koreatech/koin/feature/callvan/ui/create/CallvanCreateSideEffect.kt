package `in`.koreatech.koin.feature.callvan.ui.create

sealed interface CallvanCreateSideEffect {
    data class NavigateToDetail(val postId: Int) : CallvanCreateSideEffect
    data object ShowSubmitError : CallvanCreateSideEffect
}
