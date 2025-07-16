package `in`.koreatech.koin.feature.club.ui.clubeventmodify

import `in`.koreatech.koin.feature.club.ui.clubeventcreate.ClubEventCreateSideEffect

sealed class ClubEventModifySideEffect {
    data object EventCreateSuccess : ClubEventModifySideEffect()
    data object EventCreateFailure : ClubEventModifySideEffect()
    data object ClubImageUploadFailure : ClubEventModifySideEffect()
    data object LoadClubEventError : ClubEventModifySideEffect()
    data object MaxImageLimit : ClubEventModifySideEffect()
    data object NavigateUp : ClubEventModifySideEffect()
}
