package `in`.koreatech.koin.feature.club.ui.clubcreate

import `in`.koreatech.koin.feature.club.model.ClubCategories

data class ClubCreateState(
    val clubName: String = "",
    val clubDescription: String = "",
    val clubCategory: ClubCategories? = null,
    val isClubCategoryDropdownExpanded: Boolean = false,
    val location: String = "",
    val instagramUrl: String = "",
    val googleFormUrl: String = "",
    val openChatUrl: String = "",
    val phoneNumber: String = "",
    val shouldCheckRequiredField: Boolean = false
) {
    val clubNameRequired: Boolean
        get() = clubName.isBlank() && shouldCheckRequiredField

    val clubCategoryRequired: Boolean
        get() = clubCategory == null && shouldCheckRequiredField

    val locationRequired: Boolean
        get() = location.isBlank() && shouldCheckRequiredField
}