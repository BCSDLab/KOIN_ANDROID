package `in`.koreatech.koin.feature.lostandfound.ui.lostandfound

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LostAndFoundState(
    val isLoading: Boolean = false,
    val showLoginRequestDialog: Boolean = false,
    val isFabDialogExpanded: Boolean = false,
    val myKeywords: List<String> = emptyList(),
    val selectedKeyword: String = "",
    val isAnonymous: Boolean = false,
    val userType: String = "",
    val lostAndFoundList: List<LostAndFoundItemState> = emptyList(),
    val currentPage: Int = 1,
    val totalCount: Int = 0,
    val currentCount: Int = 0,
    val totalPage: Int = 1
) : Parcelable
