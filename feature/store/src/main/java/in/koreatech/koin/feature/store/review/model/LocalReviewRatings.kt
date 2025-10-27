package `in`.koreatech.koin.feature.store.review.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class LocalReviewRatings(
    val reviews: ImmutableList<LocalReviewRating>,
    val average: Double,
    val totalReview: Int
) {
    companion object {
        fun empty() = LocalReviewRatings(
            reviews = persistentListOf(
                LocalReviewRating(
                    rating = 5,
                    quantity = 0
                ),
                LocalReviewRating(
                    rating = 4,
                    quantity = 0
                ),
                LocalReviewRating(
                    rating = 3,
                    quantity = 0
                ),
                LocalReviewRating(
                    rating = 2,
                    quantity = 0
                ),
                LocalReviewRating(
                    rating = 1,
                    quantity = 0
                )
            ),
            average = 0.0,
            totalReview = 0
        )
    }
}

data class LocalReviewRating(
    val rating: Int,
    val quantity: Int
)
