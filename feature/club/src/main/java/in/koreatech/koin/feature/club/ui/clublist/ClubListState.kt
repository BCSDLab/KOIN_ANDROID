package `in`.koreatech.koin.feature.club.ui.clublist

import android.os.Parcelable
import `in`.koreatech.koin.feature.club.model.ClubSort
import `in`.koreatech.koin.feature.club.model.ParcelizeClubItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClubListState(
    val isInitialized: Boolean = false,
    val isLoading: Boolean = false,
    val categoryId: Int? = null,
    val sortType: ClubSort = ClubSort.NONE,
    val isDropdownExpanded: Boolean = false,
    val shouldShowClubCreateDialog: Boolean = false,
    val shouldShowLoginDialog: Boolean = false,
    val clubs: List<ParcelizeClubItem> = emptyList(),
    val searchKeyword: String = "",
    val isAnonymous: Boolean = true
) : Parcelable
