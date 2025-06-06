package `in`.koreatech.koin.feature.club.ui.clubmodify

import android.os.Parcelable
import `in`.koreatech.koin.feature.club.model.ClubCategories
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClubModifyState(
    val isLoading: Boolean = false,
    val clubId: Int = -1,
    val clubName: String = "",
    val clubDescription: String = "",
    val clubCategory: ClubCategories? = null,
    val clubImageUrl: String = "",
    val isClubImageChanged: Boolean = false,
    val isClubCategoryDropdownExpanded: Boolean = false,
    val location: String = "",
    val instagramUrl: String = "",
    val googleFormUrl: String = "",
    val openChatUrl: String = "",
    val phoneNumber: String = "",
    val shouldCheckRequiredField: Boolean = false,
    val shouldShowModifyDialog: Boolean = false,
    val userRole: String = "",
    val isLikeHidden: Boolean = false,
    val likes: Int = 0
) : Parcelable {
    val clubNameRequired: Boolean
        get() = clubName.isBlank() && shouldCheckRequiredField

    val clubCategoryRequired: Boolean
        get() = clubCategory == null && shouldCheckRequiredField

    val locationRequired: Boolean
        get() = location.isBlank() && shouldCheckRequiredField

    val clubImageUrlRequired: Boolean
        get() = clubImageUrl.isBlank() && shouldCheckRequiredField
}
