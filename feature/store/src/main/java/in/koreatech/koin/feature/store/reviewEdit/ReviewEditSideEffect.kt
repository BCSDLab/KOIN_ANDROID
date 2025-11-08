package `in`.koreatech.koin.feature.store.reviewEdit

sealed class ReviewEditSideEffect {
    data class ShowToast(val message: String) : ReviewEditSideEffect()

    object NavigateToReview : ReviewEditSideEffect()
}
