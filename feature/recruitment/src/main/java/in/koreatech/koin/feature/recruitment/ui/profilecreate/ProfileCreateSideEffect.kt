package `in`.koreatech.koin.feature.recruitment.ui.profilecreate

sealed interface ProfileCreateSideEffect {
    data object NavigateUp : ProfileCreateSideEffect
    data object SaveSuccess : ProfileCreateSideEffect
    data object SaveFailure : ProfileCreateSideEffect
}
