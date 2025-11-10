package `in`.koreatech.koin.feature.store.reviewedit

sealed class ReviewEditSideEffect {
    object ShowImageUploadFailed : ReviewEditSideEffect()
    object ShowReviewModified : ReviewEditSideEffect()
    object ShowReviewModifyFailed : ReviewEditSideEffect()

    object NavigateToReview : ReviewEditSideEffect()
}
