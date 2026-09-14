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
    val selectedLocation: RecruitmentLocation? = null
) {
    val hasVisibleChips: Boolean
        get() = selectedStatus != null ||
            selectedCategories.isNotEmpty() ||
            selectedLocation != null
}

enum class RecruitmentSort(
    @StringRes val labelRes: Int,
    val apiValue: String
) {
    LATEST(R.string.recruitment_filter_sort_latest, "LATEST_DESC"),
    DEADLINE_SOON(R.string.recruitment_filter_sort_deadline, "DEADLINE_ASC");

    companion object {
        val ALL: ImmutableList<RecruitmentSort> = entries.toImmutableList()
    }
}
