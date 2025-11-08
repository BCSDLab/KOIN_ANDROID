package `in`.koreatech.koin.feature.store.reviewadd

sealed class ReviewAddSideEffect {
    data class ShowToast(val message: String) : ReviewAddSideEffect()

    object NavigateToReview : ReviewAddSideEffect()
}
