package `in`.koreatech.koin.feature.article.ui.lostandfound.list

import android.os.Parcelable
import `in`.koreatech.koin.feature.article.enums.LostOrFoundType
import kotlinx.parcelize.Parcelize

@Parcelize
data class LostAndFoundState(
    val isLoading: Boolean = false,
    val showLoginRequestDialog: Boolean = false,
    val isFabDialogExpanded: Boolean = false,
    val isDropdownExpanded: Boolean = false,
    val myKeywords: List<String> = emptyList(),
    val selectedKeyword: String = "",
    val selectedType: LostOrFoundType? = null,
    val isAnonymous: Boolean = false,
    val userType: String = "",
    val lostAndFoundList: List<LostAndFoundItemState> = emptyList(),
    val currentPage: Int = 1,
    val totalCount: Int = 0,
    val currentCount: Int = 0,
    val totalPage: Int = 1
) : Parcelable
