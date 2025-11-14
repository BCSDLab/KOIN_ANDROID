package `in`.koreatech.koin.feature.store.review

import `in`.koreatech.koin.feature.store.model.StoreNavigationData
import `in`.koreatech.koin.feature.store.review.model.LocalReviewContent
import `in`.koreatech.koin.feature.store.review.model.LocalReviewRatings
import `in`.koreatech.koin.feature.store.review.model.ReviewOrderOption
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
data class ReviewState(
    val isLoading: Boolean = false,
    val storeNavigationData: StoreNavigationData = StoreNavigationData(-1, -1, false),
    val storeName: String = "",
    val reviewRatings: LocalReviewRatings = LocalReviewRatings.empty(),
    val reviews: ImmutableList<LocalReviewContent> = persistentListOf(),
    val orderOption: OrderOption = OrderOption(),
    val showDeleteDialog: DeleteDialogState = DeleteDialogState.Hide,
    val filterMyReview: Boolean = false
) {
    @Serializable
    data class OrderOption(
        val showOrderOptionChooser: Boolean = false,
        val reviewOrderOption: ReviewOrderOption = ReviewOrderOption.RECENT
    )

    @Serializable
    sealed class DeleteDialogState {
        data object Hide : DeleteDialogState()
        data class Show(val reviewId: Int) : DeleteDialogState()
    }
}
