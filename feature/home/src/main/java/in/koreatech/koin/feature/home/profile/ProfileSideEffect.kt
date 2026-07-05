package `in`.koreatech.koin.feature.home.profile

sealed class ProfileSideEffect {
    data object LogoutSuccess : ProfileSideEffect()
}
