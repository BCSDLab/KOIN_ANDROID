package `in`.koreatech.koin.feature.store.review

import `in`.koreatech.koin.feature.store.model.StoreNavigationData
import `in`.koreatech.koin.feature.store.review.model.LocalReviewRatings
import `in`.koreatech.koin.feature.store.review.model.ReviewOrderOption
import kotlinx.serialization.Serializable

@Serializable
data class ReviewState(
    val isLoading: Boolean = false,
    val storeNavigationData: StoreNavigationData = StoreNavigationData(-1, -1, false),
    val reviewRatings: LocalReviewRatings = LocalReviewRatings.empty(),
    val orderOption: OrderOption = OrderOption(),
    val filterMyReview: Boolean = false
) {
    @Serializable
    data class OrderOption(
        val showOrderOptionChooser: Boolean = false,
        val reviewOrderOption: ReviewOrderOption = ReviewOrderOption.RECENT
    )
}
