package `in`.koreatech.koin.feature.store.reviewadd

sealed class ReviewAddSideEffect {
    object ShowImageUploadFailed : ReviewAddSideEffect()
    object ShowReviewWritten : ReviewAddSideEffect()
    object ShowReviewWriteFailed : ReviewAddSideEffect()
    object ShowOneReviewPerDay : ReviewAddSideEffect()

    object NavigateToReview : ReviewAddSideEffect()
}
