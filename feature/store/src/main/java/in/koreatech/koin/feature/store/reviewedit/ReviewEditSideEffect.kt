package `in`.koreatech.koin.feature.store.reviewedit

sealed class ReviewEditSideEffect {
    data class ShowToast(val message: String) : ReviewEditSideEffect()
    data class ShowImageUploadFailedToast(val message: String = "이미지 업로드 실패") : ReviewEditSideEffect()
    data class ShowRatingValidationToast(val message: String = "별점을 1점 이상 선택해주세요.") : ReviewEditSideEffect()
    data class ShowReviewModifiedToast(val message: String = "리뷰가 수정되었어요") : ReviewEditSideEffect()
    data class ShowReviewModifyFailedToast(val message: String = "리뷰 수정에 실패했습니다.") : ReviewEditSideEffect()

    object NavigateToReview : ReviewEditSideEffect()
}
