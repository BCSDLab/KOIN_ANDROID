package `in`.koreatech.koin.feature.store.review

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.store.GetStoreReviewUseCase
import `in`.koreatech.koin.feature.store.model.StoreNavigationData
import `in`.koreatech.koin.feature.store.model.StoreNavigationDataType
import `in`.koreatech.koin.feature.store.navigation.StoreReviewNavType
import `in`.koreatech.koin.feature.store.review.model.ReviewOrderOption
import `in`.koreatech.koin.feature.store.review.model.toLocalReviewContent
import `in`.koreatech.koin.feature.store.review.model.toLocalReviewRatings
import javax.inject.Inject
import kotlin.reflect.typeOf
import kotlinx.collections.immutable.toImmutableList
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class ReviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getStoreReviewUseCase: GetStoreReviewUseCase
) : ViewModel(), ContainerHost<ReviewState, ReviewSideEffect> {
    override val container = container<ReviewState, ReviewSideEffect>(ReviewState()) {
        val route = savedStateHandle.toRoute<StoreReviewNavType.StoreReviewHome>(
            typeMap = mapOf(typeOf<StoreNavigationData>() to StoreNavigationDataType)
        )

        blockingIntent {
            reduce {
                state.copy(
                    storeNavigationData = route.storeNavigationData,
                    storeName = route.storeName
                )
            }
        }
    }

    init {
        fetchReviews()
    }

    fun showReviewOrderOptionChooser() = intent {
        reduce {
            state.copy(
                orderOption = state.orderOption.copy(
                    showOrderOptionChooser = true
                )
            )
        }
    }

    fun hideReviewOrderOptionChooser() = intent {
        reduce {
            state.copy(
                orderOption = state.orderOption.copy(
                    showOrderOptionChooser = false
                )
            )
        }
    }

    fun setReviewOrderOption(reviewOrderOption: ReviewOrderOption) = intent {
        reduce {
            state.copy(
                orderOption = ReviewState.OrderOption(
                    showOrderOptionChooser = false,
                    reviewOrderOption = reviewOrderOption
                )
            )
        }
    }

    fun setFilterMyReview(filterMyReview: Boolean) = intent {
        reduce {
            state.copy(
                filterMyReview = filterMyReview
            )
        }
    }

    fun orderReviews(reviewOrderOption: ReviewOrderOption) = intent {
        reduce {
            state.copy(
                reviews = when (reviewOrderOption) {
                    ReviewOrderOption.RECENT -> state.reviews.sortedByDescending { it.createdAt }
                    ReviewOrderOption.LEAST_RECENT -> state.reviews.sortedBy { it.createdAt }
                    ReviewOrderOption.HIGHER_RATING -> state.reviews.sortedByDescending { it.rating }
                    ReviewOrderOption.LOWER_RATING -> state.reviews.sortedBy { it.rating }
                }.toImmutableList()
            )
        }
    }

    private fun fetchReviews() = intent {
        reduce {
            state.copy(isLoading = true)
        }
        getStoreReviewUseCase(shopId = state.storeNavigationData.shopId).let { data ->
            reduce {
                state.copy(
                    isLoading = false,
                    reviewRatings = data.toLocalReviewRatings(),
                    reviews = data.reviews.map { it.toLocalReviewContent() }.toImmutableList()
                )
            }
        }
    }
}
