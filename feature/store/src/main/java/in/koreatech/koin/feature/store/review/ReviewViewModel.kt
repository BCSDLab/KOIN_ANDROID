package `in`.koreatech.koin.feature.store.review

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.feature.store.model.StoreNavigationData
import `in`.koreatech.koin.feature.store.model.StoreNavigationDataType
import `in`.koreatech.koin.feature.store.navigation.StoreReviewNavType
import `in`.koreatech.koin.feature.store.review.model.ReviewOrderOption
import javax.inject.Inject
import kotlin.reflect.typeOf
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class ReviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel(), ContainerHost<ReviewState, ReviewSideEffect> {
    override val container = container<ReviewState, ReviewSideEffect>(ReviewState()) {
        val storeNavigationData = savedStateHandle.toRoute<StoreReviewNavType.StoreReviewHome>(
            typeMap = mapOf(typeOf<StoreNavigationData>() to StoreNavigationDataType)
        ).storeNavigationData

        blockingIntent {
            reduce {
                state.copy(
                    storeNavigationData = storeNavigationData
                )
            }
        }
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
}
