package `in`.koreatech.koin.feature.store.review

sealed class ReviewSideEffect {
    data object ReviewDeleted : ReviewSideEffect()
}
