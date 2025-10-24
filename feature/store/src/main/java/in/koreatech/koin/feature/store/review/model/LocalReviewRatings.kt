package `in`.koreatech.koin.feature.store.review.model

import kotlinx.collections.immutable.ImmutableList

data class LocalReviewRatings(
    val reviews: ImmutableList<LocalReviewRating>,
    val average: Double,
    val totalReview: Int
)

data class LocalReviewRating(
    val rating: Int,
    val quantity: Int
)
