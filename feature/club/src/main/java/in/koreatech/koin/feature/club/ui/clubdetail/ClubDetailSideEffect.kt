package `in`.koreatech.koin.feature.club.ui.clubdetail

sealed class ClubDetailSideEffect {
    object ShowEmpowermentSnackBar : ClubDetailSideEffect()
    data class OpenUrl(val url: String) : ClubDetailSideEffect()
    data class ShowToast(val text: String) : ClubDetailSideEffect()
}
