package `in`.koreatech.koin.feature.store.reviewreport

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.domain.usecase.store.ReportStoreReviewUseCase
import `in`.koreatech.koin.feature.store.model.StoreNavigationData
import `in`.koreatech.koin.feature.store.model.StoreNavigationDataType
import `in`.koreatech.koin.feature.store.navigation.StoreReviewNavType
import `in`.koreatech.koin.feature.store.reviewreport.enums.ReportReason
import `in`.koreatech.koin.feature.store.reviewreport.enums.toStoreReport
import javax.inject.Inject
import kotlin.reflect.typeOf
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

@HiltViewModel
class ReviewReportViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val reportStoreReviewUseCase: ReportStoreReviewUseCase
) : ViewModel(), ContainerHost<ReviewReportState, ReviewReportSideEffect> {
    override val container = container<ReviewReportState, ReviewReportSideEffect>(ReviewReportState()) {
        val route = savedStateHandle.toRoute<StoreReviewNavType.StoreReviewReport>(
            typeMap = mapOf(typeOf<StoreNavigationData>() to StoreNavigationDataType)
        )

        blockingIntent {
            reduce {
                state.copy(
                    storeNavigationData = route.storeNavigationData,
                    reviewId = route.reviewId
                )
            }
        }
    }

    fun reportReview() = intent {
        EventLogger.logClickEvent(
            EventAction.BUSINESS,
            AnalyticsConstant.Label.SHOP_DETAIL_VIEW_REVIEW_REPORT,
            state.selectedReasons.appendLoggingString(state.etcReason)
        )
        reduce {
            state.copy(isLoading = true)
        }
        reportStoreReviewUseCase(
            storeId = state.storeNavigationData.shopId,
            reviewId = state.reviewId,
            reportList = state.selectedReasons.map { it.toStoreReport() },
            isNotRelation = state.selectedReasons.contains(ReportReason.OFF_TOPIC),
            isSpam = state.selectedReasons.contains(ReportReason.SPAM),
            isAbuse = state.selectedReasons.contains(ReportReason.INAPPROPRIATE_LANGUAGE),
            isPrivate = state.selectedReasons.contains(ReportReason.PRIVACY),
            isEtc = state.selectedReasons.contains(ReportReason.OTHER),
            etcReason = state.etcReason
        ).onSuccess {
            reduce {
                state.copy(isLoading = false)
            }
            postSideEffect(ReviewReportSideEffect.ReviewReportSuccess)
        }.onFailure {
            reduce {
                state.copy(isLoading = false)
            }
            Timber.e(it)
        }
    }

    fun addReportReason(reportReason: ReportReason) = blockingIntent {
        reduce {
            state.copy(selectedReasons = persistentListOf(reportReason).addAll(state.selectedReasons))
        }
    }

    fun updateOtherReportReason(reason: String) = blockingIntent {
        reduce {
            state.copy(etcReason = reason)
        }
    }

    fun removeReportReason(reportReason: ReportReason) = blockingIntent {
        reduce {
            state.copy(selectedReasons = persistentListOf<ReportReason>().addAll(state.selectedReasons.filter { it != reportReason }))
        }
    }

    private fun ImmutableList<ReportReason>.appendLoggingString(otherReason: String): String {
        val log = StringBuilder()
        for (reason in this) {
            if (reason == ReportReason.OTHER) {
                log.append(otherReason)
                log.append(" ")
            } else {
                log.append(reason.title)
                log.append(" ")
            }
        }

        return log.toString()
    }
}
