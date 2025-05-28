package `in`.koreatech.koin.feature.club.model

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.club.R

enum class ClubSort(val value: String, @StringRes val stringRes: Int) {
    NONE("NONE", R.string.club_list_dropdown_sort_none),
    HITS_DESC("HITS_DESC", R.string.club_list_dropdown_sort_hits)
}

val clubSortType = listOf(
    ClubSort.NONE,
    ClubSort.HITS_DESC
)
