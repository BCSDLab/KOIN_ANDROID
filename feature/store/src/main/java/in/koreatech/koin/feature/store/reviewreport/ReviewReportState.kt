package `in`.koreatech.koin.feature.store.reviewreport

import `in`.koreatech.koin.feature.store.model.StoreNavigationData
import `in`.koreatech.koin.feature.store.reviewreport.enums.ReportReason
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
data class ReviewReportState(
    val isLoading: Boolean = false,
    val storeNavigationData: StoreNavigationData = StoreNavigationData(-1, -1, false),
    val reviewId: Int? = null,
    val selectedReasons: ImmutableList<ReportReason> = persistentListOf(),
    val etcReason: String = ""
)
