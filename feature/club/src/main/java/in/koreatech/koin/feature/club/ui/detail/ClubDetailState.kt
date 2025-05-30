package `in`.koreatech.koin.feature.club.ui.detail

import `in`.koreatech.koin.domain.model.club.ClubDetails
import `in`.koreatech.koin.domain.model.club.ClubQnasInfo

data class ClubDetailState(
    val isLoading: Boolean = false,
    val userId: Int? = null,
    val clubDetails: ClubDetails? = null,
    val clubQnasInfo: ClubQnasInfo? = null,
    val showLoginDialog: Boolean = false,
    val showAddQnaDialog: Boolean = false,
    val showEmpowermentDialog: Boolean = false,
    val textFieldErrorResId: Int? = null
)
