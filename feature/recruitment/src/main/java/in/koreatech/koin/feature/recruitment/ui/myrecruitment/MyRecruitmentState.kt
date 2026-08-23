package `in`.koreatech.koin.feature.recruitment.ui.myrecruitment

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model.MyRecruitmentPost
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model.RecruitmentFilterState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class MyRecruitmentState(
    val posts: ImmutableList<MyRecruitmentPost> = persistentListOf(),
    val filter: RecruitmentFilterState = RecruitmentFilterState(),
    val pendingFilter: RecruitmentFilterState = RecruitmentFilterState(),
    val showCloseDialog: Boolean = false,
    val closeTargetPostId: Long? = null,
    val showFilterSheet: Boolean = false
)
