package `in`.koreatech.koin.feature.club.ui.clubcreate

sealed class ClubCreateSideEffect {
    data object ClubCreateSuccess : ClubCreateSideEffect()
    data object ClubCreateFailure : ClubCreateSideEffect()
    data object ClubImageUploadFailure : ClubCreateSideEffect()
}
