package `in`.koreatech.koin.feature.club.ui.clubdetail

import android.os.Parcelable
import `in`.koreatech.koin.feature.club.model.ParcelizeClubDetails
import `in`.koreatech.koin.feature.club.model.ParcelizeClubQnasInfo
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClubDetailState(
    val isLoading: Boolean = false,
    val userId: Int? = null,
    val userLoginId: String? = null,
    val clubId: Int = -1,
    val clubDetails: ParcelizeClubDetails? = null,
    val clubQnasInfo: ParcelizeClubQnasInfo? = null,
    val showQnasProgressBar: Boolean = false,
    val showLoginDialog: Boolean = false,
    val showAddQnaDialog: Boolean = false,
    val showEmpowermentDialog: Boolean = false,
    val showImageDialog: Boolean = false,
    val textFieldErrorMessageResId: Int? = null
) : Parcelable
