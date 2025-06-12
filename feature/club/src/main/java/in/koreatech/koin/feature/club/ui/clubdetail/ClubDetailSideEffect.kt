package `in`.koreatech.koin.feature.club.ui.clubdetail

import `in`.koreatech.koin.feature.club.R

sealed class ClubDetailSideEffect {
    object ShowEmpowermentSnackBar : ClubDetailSideEffect()
    data class OpenUrl(val url: String) : ClubDetailSideEffect()
    object UnauthorizedError : ClubDetailSideEffect() {
        val messageResId = R.string.detail_error_unauthorized
    }
    object DeletePermissionDeniedError : ClubDetailSideEffect() {
        val messageResId = R.string.detail_error_delete_permission_denied
    }
    object ClubNotFoundError : ClubDetailSideEffect() {
        val messageResId = R.string.detail_error_club_not_found
    }
    object NotClubManagerError : ClubDetailSideEffect() {
        val messageResId = R.string.detail_error_not_club_manager
    }
    object QnaNotFoundError : ClubDetailSideEffect() {
        val messageResId = R.string.detail_error_qna_not_found
    }
    object AlreadyLikedError : ClubDetailSideEffect() {
        val messageResId = R.string.detail_error_already_liked
    }
    object AlreadyNotLikedError : ClubDetailSideEffect() {
        val messageResId = R.string.detail_error_already_not_liked
    }
}
