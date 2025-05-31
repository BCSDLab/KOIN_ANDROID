package `in`.koreatech.koin.feature.club.ui.detail

sealed class ClubDetailSideEffect {
    object ShowEmpowermentSnackBar : ClubDetailSideEffect()
    data class OpenUrl(val url: String) : ClubDetailSideEffect()
}
