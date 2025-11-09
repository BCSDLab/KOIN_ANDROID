package `in`.koreatech.koin.feature.store.reviewadd

sealed class ReviewAddSideEffect {
    data class ShowToast(val message: String) : ReviewAddSideEffect()
    data class ShowImageUploadFailedToast(val message: String = "이미지 업로드 실패") : ReviewAddSideEffect()
    data class ShowRatingValidationToast(val message: String = "별점을 1점 이상 선택해주세요.") : ReviewAddSideEffect()
    data class ShowReviewWrittenToast(val message: String = "리뷰가 작성되었어요") : ReviewAddSideEffect()
    data class ShowReviewWriteFailedToast(val message: String = "리뷰 작성에 실패했습니다.") : ReviewAddSideEffect()
    data class ShowOneReviewPerDayToast(val message: String = "한 상점에 하루에 한번만 리뷰를 남길 수 있습니다.") : ReviewAddSideEffect()

    object NavigateToReview : ReviewAddSideEffect()
}
