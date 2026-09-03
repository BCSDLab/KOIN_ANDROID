package `in`.koreatech.koin.feature.recruitment.ui.main

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.feature.recruitment.ui.main.model.RecruitmentFilterState
import `in`.koreatech.koin.feature.recruitment.ui.main.model.RecruitmentItemModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class RecruitmentMainState(
    val searchValue: String = "",
    val items: ImmutableList<RecruitmentItemModel> = persistentListOf(),
    val totalCount: Long = 0,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val filterState: RecruitmentFilterState = RecruitmentFilterState(),
    val pendingFilterState: RecruitmentFilterState = RecruitmentFilterState(),
    val isFilterVisible: Boolean = false
)
