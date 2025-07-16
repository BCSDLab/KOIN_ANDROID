package `in`.koreatech.koin.feature.club.ui.clubdetail

import android.os.Parcelable
import `in`.koreatech.koin.feature.club.model.ParcelizeClubDetails
import `in`.koreatech.koin.feature.club.model.ParcelizeClubEvent
import `in`.koreatech.koin.feature.club.model.ParcelizeClubQnasInfo
import `in`.koreatech.koin.feature.club.model.ParcelizeClubRecruitment
import `in`.koreatech.koin.feature.club.type.EventSearchType
import kotlinx.parcelize.Parcelize
import okhttp3.internal.immutableListOf

@Parcelize
data class ClubDetailState(
    val isLoading: Boolean = false,
    val userId: Int? = null,
    val userLoginId: String? = null,
    val clubId: Int = -1,
    val clubDetails: ParcelizeClubDetails? = null,
    val clubQnasInfo: ParcelizeClubQnasInfo? = null,
    val showQnasProgressBar: Boolean = false,
    val showRecruitProgressBar: Boolean = false,
    val showLoginDialog: Boolean = false,
    val showAddQnaDialog: Boolean = false,
    val showEmpowermentDialog: Boolean = false,
    val showImageDialog: Boolean = false,
    val imageDialogUrl: String = "",
    val textFieldErrorMessageResId: Int? = null,
    val clubRecruitment: ParcelizeClubRecruitment? = null,
    val showRecruitDeleteDialog: Boolean = false,
    val isEventsDropdownExpanded: Boolean = false,
    val showEventDeleteDialog: Boolean = false,
    val clubEventSelected: Boolean = false,
    val selectedEventIndex: Int = -1,
    val clubEventSearchType: EventSearchType = EventSearchType.RECENT,
    val clubEvents: List<ParcelizeClubEvent> = immutableListOf(),
    val showEventsProgressBar: Boolean = false,
    val showRecruitSubscribeDialog: Boolean = false,
    val showEventSubscribeDialog: Boolean = false,
    val clubEventLoaded: Boolean = false
) : Parcelable
