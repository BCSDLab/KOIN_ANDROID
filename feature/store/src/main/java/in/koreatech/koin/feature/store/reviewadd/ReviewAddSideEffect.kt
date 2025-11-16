package `in`.koreatech.koin.feature.store.reviewadd

sealed class ReviewAddSideEffect {
    object ShowImageUploadFailed : ReviewAddSideEffect()
    object ShowReviewWritten : ReviewAddSideEffect()
    object ShowReviewWriteFailed : ReviewAddSideEffect()
    object ShowReviewWriteIsNotBlank : ReviewAddSideEffect()
    object ShowOneReviewPerDay : ReviewAddSideEffect()

    object NavigateToReview : ReviewAddSideEffect()
    object ReviewUpdated : ReviewAddSideEffect()
}
