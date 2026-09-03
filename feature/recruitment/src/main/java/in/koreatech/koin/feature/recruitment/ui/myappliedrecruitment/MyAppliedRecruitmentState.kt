package `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.model.AppliedFilterState
import `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.model.AppliedRecruitmentPost
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class MyAppliedRecruitmentState(
    val posts: ImmutableList<AppliedRecruitmentPost> = persistentListOf(),
    val filter: AppliedFilterState = AppliedFilterState(),
    val currentPage: Int = 1,
    val totalPage: Int = 1,
    val isLoading: Boolean = true,
    val isLoadingMore: Boolean = false,
    val showFilterSheet: Boolean = false
)
