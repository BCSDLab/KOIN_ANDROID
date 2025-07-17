package `in`.koreatech.koin.feature.club.ui.clubeventcreate

sealed class ClubEventCreateSideEffect {
    data object EventCreateSuccess : ClubEventCreateSideEffect()
    data object EventCreateFailure : ClubEventCreateSideEffect()
    data object ClubImageUploadFailure : ClubEventCreateSideEffect()
    data object NavigateUp : ClubEventCreateSideEffect()
}
