package `in`.koreatech.koin.feature.profile

sealed class ProfileSideEffect {
    data object LogoutSuccess : ProfileSideEffect()
}
