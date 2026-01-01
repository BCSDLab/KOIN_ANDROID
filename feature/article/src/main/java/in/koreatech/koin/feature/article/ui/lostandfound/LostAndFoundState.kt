package `in`.koreatech.koin.feature.article.ui.lostandfound

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LostAndFoundState(
    val isLoading: Boolean = false,
    val showLoginRequestDialog: Boolean = false,
    val isFabDialogExpanded: Boolean = false,
    val isDropdownExpanded: Boolean = false,
    val isAnonymous: Boolean = false,
    val userType: String = ""
) : Parcelable
