package `in`.koreatech.koin.feature.club.ui.clublist

sealed class ClubListSideEffect {
    data object RefreshClubs : ClubListSideEffect()
    data object NavigateToCreateClub : ClubListSideEffect()
    data object ClubsFetchFailed : ClubListSideEffect()
    data object ClubLikeFailed : ClubListSideEffect()
    data object ClubDislikeFailed : ClubListSideEffect()
}
