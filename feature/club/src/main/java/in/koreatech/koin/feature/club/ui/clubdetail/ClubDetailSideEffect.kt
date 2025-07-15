package `in`.koreatech.koin.feature.club.ui.clubdetail

import `in`.koreatech.koin.feature.club.R

sealed class ClubDetailSideEffect {
    data object ShowEmpowermentSnackBar : ClubDetailSideEffect()
    data class OpenUrl(val url: String) : ClubDetailSideEffect()
    data object UnauthorizedError : ClubDetailSideEffect() {
        val messageResId = R.string.detail_error_unauthorized
    }
    data object DeletePermissionDeniedError : ClubDetailSideEffect() {
        val messageResId = R.string.detail_error_delete_permission_denied
    }
    data object ClubNotFoundError : ClubDetailSideEffect() {
        val messageResId = R.string.detail_error_club_not_found
    }
    data object NotClubManagerError : ClubDetailSideEffect() {
        val messageResId = R.string.detail_error_not_club_manager
    }
    data object QnaNotFoundError : ClubDetailSideEffect() {
        val messageResId = R.string.detail_error_qna_not_found
    }
    data object AlreadyLikedError : ClubDetailSideEffect() {
        val messageResId = R.string.detail_error_already_liked
    }
    data object AlreadyNotLikedError : ClubDetailSideEffect() {
        val messageResId = R.string.detail_error_already_not_liked
    }
    data object DeleteClubRecruitmentError : ClubDetailSideEffect() {
        val messageResId = R.string.detail_error_delete_recruit
    }
    data object LoadClubRecruitmentError : ClubDetailSideEffect() {
        val messageResId = R.string.detail_error_load_recruit
    }
    data object LoadClubEventError : ClubDetailSideEffect() {
        val messageResId = R.string.detail_error_load_events
    }
    data object UnknownError : ClubDetailSideEffect() {
        val messageResId = R.string.detail_error_unkown
    }
}
