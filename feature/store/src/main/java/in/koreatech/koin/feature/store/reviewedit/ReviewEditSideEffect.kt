package `in`.koreatech.koin.feature.store.reviewedit

sealed class ReviewEditSideEffect {
    object ShowImageUploadFailed : ReviewEditSideEffect()
    object ShowReviewModified : ReviewEditSideEffect()
    object ShowReviewModifyFailed : ReviewEditSideEffect()
    object ShowReviewModifyIsNotBlank : ReviewEditSideEffect()

    object NavigateToReview : ReviewEditSideEffect()
    object ReviewUpdated : ReviewEditSideEffect()
}
