package `in`.koreatech.koin.feature.club.ui.clubcreate

sealed class ClubCreateSideEffect {
    data object ClubCreateSuccess : ClubCreateSideEffect()
}