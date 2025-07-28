package `in`.koreatech.koin.feature.club.model

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.club.R

enum class ClubSort(val value: String, @StringRes val stringRes: Int) {
    CREATED_AT_ASC("CREATED_AT_ASC", R.string.club_list_dropdown_sort_none),
    HITS_DESC("HITS_DESC", R.string.club_list_dropdown_sort_hits),
    RECRUITMENT_UPDATED_DESC("RECRUITMENT_UPDATED_DESC", R.string.club_list_dropdown_sort_recruitment_updated_desc),
    RECRUITING_DEADLINE_ASC("RECRUITING_DEADLINE_ASC", R.string.club_list_dropdown_sort_recruitment_deadline_asc)
}

val clubSortType = listOf(
    ClubSort.CREATED_AT_ASC,
    ClubSort.HITS_DESC
)

val clubSortTypeWithRecruitment = listOf(
    ClubSort.RECRUITMENT_UPDATED_DESC,
    ClubSort.RECRUITING_DEADLINE_ASC
)
