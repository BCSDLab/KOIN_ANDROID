package `in`.koreatech.koin.feature.recruitment.ui.main.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.feature.recruitment.R
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

enum class RecruitmentStatus(
    @StringRes val labelRes: Int
) {
    RECRUITING(R.string.recruitment_filter_status_recruiting),
    COMPLETED(R.string.recruitment_filter_status_complete);

    companion object {
        val ALL: ImmutableList<RecruitmentStatus> = entries.toImmutableList()
    }
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

enum class RecruitmentCategory(
    @StringRes val labelRes: Int
) {
    CONTEST(R.string.recruitment_category_contest),
    ACTIVITY(R.string.recruitment_category_activity),
    STUDY(R.string.recruitment_category_study),
    PROJECT(R.string.recruitment_category_project),
    ETC(R.string.recruitment_category_etc);

    companion object {
        val ALL: ImmutableList<RecruitmentCategory> = entries.toImmutableList()
    }
}

enum class RecruitmentLocation(
    @StringRes val labelRes: Int
) {
    ONLINE(R.string.recruitment_filter_location_online),
    OFFLINE(R.string.recruitment_filter_location_offline),
    MIXED(R.string.recruitment_filter_location_mixed);

    companion object {
        val ALL: ImmutableList<RecruitmentLocation> = entries.toImmutableList()
    }
}
