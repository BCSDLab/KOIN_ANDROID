package `in`.koreatech.koin.domain.state.store

sealed class StoreReviewState {
    object ReportComplete : StoreReviewState()
}

sealed class StoreReviewExceptionState: Throwable() {
    object ToastNullCheckBox : StoreReviewExceptionState()
    object ToastNullEtcReason : StoreReviewExceptionState()
}