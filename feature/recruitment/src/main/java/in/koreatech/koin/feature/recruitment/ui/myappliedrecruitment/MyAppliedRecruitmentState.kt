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
    val pendingFilter: AppliedFilterState = AppliedFilterState(),
    val showFilterSheet: Boolean = false
)
