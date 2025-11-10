package `in`.koreatech.koin.feature.store.review.model

import `in`.koreatech.koin.domain.model.store.StoreReview
import `in`.koreatech.koin.domain.model.store.StoreReviewStatistics
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
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

@Serializable
data class LocalReviewRating(
    val rating: Int,
    val quantity: Int
)

internal fun StoreReviewStatistics.toLocalReviewRating() = persistentListOf(
    LocalReviewRating(
        rating = 5,
        quantity = ratings["5"] ?: 0
    ),
    LocalReviewRating(
        rating = 4,
        quantity = ratings["4"] ?: 0
    ),
    LocalReviewRating(
        rating = 3,
        quantity = ratings["3"] ?: 0
    ),
    LocalReviewRating(
        rating = 2,
        quantity = ratings["2"] ?: 0
    ),
    LocalReviewRating(
        rating = 1,
        quantity = ratings["1"] ?: 0
    )
)

internal fun StoreReview.toLocalReviewRatings() = LocalReviewRatings(
    reviews = statistics.toLocalReviewRating(),
    average = statistics.averageRating,
    totalReview = totalCount
)
