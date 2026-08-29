package `in`.koreatech.koin.feature.recruitment.ui.main.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentLocation
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentStatus
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Immutable
data class RecruitmentFilterState(
    val selectedStatus: RecruitmentStatus? = null,
    val selectedSort: RecruitmentSort = RecruitmentSort.LATEST,
    val selectedCategories: ImmutableList<RecruitmentCategory> = persistentListOf(),
    val selectedLocations: ImmutableList<RecruitmentLocation> = persistentListOf()
) {
    val hasVisibleChips: Boolean
        get() = selectedStatus != null ||
            selectedCategories.isNotEmpty() ||
            selectedLocations.isNotEmpty()
}

enum class RecruitmentSort(
    @StringRes val labelRes: Int
) {
    LATEST(R.string.recruitment_filter_sort_latest),
    DEADLINE_SOON(R.string.recruitment_filter_sort_deadline);

    companion object {
        val ALL: ImmutableList<RecruitmentSort> = entries.toImmutableList()
    }
}
